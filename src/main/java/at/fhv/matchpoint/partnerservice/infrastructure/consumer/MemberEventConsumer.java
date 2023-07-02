package at.fhv.matchpoint.partnerservice.infrastructure.consumer;

import at.fhv.matchpoint.partnerservice.application.LockPartnerRequestService;
import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.events.member.MemberAddedEvent;
import at.fhv.matchpoint.partnerservice.events.member.MemberEvent;
import at.fhv.matchpoint.partnerservice.events.member.MemberLockedEvent;
import at.fhv.matchpoint.partnerservice.events.member.MemberUnlockedEvent;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.MemberEventTrackingRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.MemberRepository;
import at.fhv.matchpoint.partnerservice.utils.*;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MessageAlreadyProcessedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.panache.common.Sort;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.stream.StreamMessage;
import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;

@ApplicationScoped
public class MemberEventConsumer {

    @Inject
    RedisDataSource redisDataSource;

    @Inject
    ObjectMapper mapper;

    @Inject
    MemberRepository memberRepository;

    @Inject
    LockPartnerRequestService lockPartnerRequestService;

    @Inject
    MemberEventTrackingRepository memberEventTrackingRepository;

    private static final Logger LOGGER = Logger.getLogger(MemberEventConsumer.class);

    final String GROUP_NAME = "partnerService";
    final String STREAM_KEY = "memberservice.public.event";
    final String CONSUMER = UUID.randomUUID().toString();
    final Class<JsonNode> TYPE = JsonNode.class;
    final String PAYLOAD_KEY = "value";

    // create group for horizontal scaling. this way each partner service instance doesnt ready messages multiple times
    @PostConstruct
    public void createGroup() {
        try {
            redisDataSource.stream(TYPE).xgroupCreate(STREAM_KEY, GROUP_NAME, "0", new XGroupCreateArgs().mkstream());
        } catch (Exception e) {
            LOGGER.info("Group already exists");
        }
    }

    //constantly checks if new messages are available to be read and processed
    @Scheduled(every = "0s")
    void readMessage() {
        // > reads only messages that have not been read by any other consumer of the group
        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">");
        for (StreamMessage<String, String, JsonNode> message : messages) {
            Entry<String, JsonNode> payload = message.payload().entrySet().stream().findFirst().get();
            try {
                MemberEvent memberEvent = mapper.readValue(payload.getValue().get("payload").get("after").toString(), MemberEvent.class);
                handleEvent(memberEvent);
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());                
            } catch (MemberNotFoundException e) {
                readAllMessages(e);
            } catch (MessageAlreadyProcessedException e) {
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    void readAllMessages(MemberNotFoundException exception) {
        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, "0");

        for (StreamMessage<String, String, JsonNode> message : messages) {
            Map<String, JsonNode> payload = message.payload();
            try {
                MemberEvent memberEvent = mapper.readValue(payload.get(PAYLOAD_KEY).get("payload").get("after").asText(), MemberEvent.class);
                handleEvent(memberEvent);
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (MemberNotFoundException e) {
                LOGGER.info(exception.getMessage());
            } catch (MessageAlreadyProcessedException e) {
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
    }


    // when consumer fails, claim the open messages
    @Scheduled(every = "600s")
    void claimOpenMessages() {
        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600), "0").getMessages();

        for (StreamMessage<String, String, JsonNode> message : messages) {
            Map<String, JsonNode> payload = message.payload();
            try {
                MemberEvent memberEvent = mapper.readValue(payload.get(PAYLOAD_KEY).get("payload").get("after").asText(), MemberEvent.class);
                handleEvent(memberEvent);
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (MessageAlreadyProcessedException e) {
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    @Transactional
    public void handleEvent(MemberEvent memberEvent) throws MemberNotFoundException, MessageAlreadyProcessedException {
        MemberEvent lastProcessedEvent;
        try {
            memberEventTrackingRepository.persist(memberEvent);
            lastProcessedEvent = memberEventTrackingRepository.find("entity_id", Sort.by("timestamp").descending(), memberEvent.entity_id).firstResult();
        } catch (Exception e) {
            throw new MessageAlreadyProcessedException();
        }
        Optional<Member> optMember = memberRepository.findByIdOptional(memberEvent.entity_id);
        Member member = new Member();
        if (optMember.isPresent()) {
            member = optMember.get();
        }
        member = build(member, memberEvent, lastProcessedEvent);
        memberRepository.persistAndFlush(member);
    }

    private Member build(Member model, MemberEvent memberEvent, MemberEvent lastProcessedEvent) throws MemberNotFoundException {
        Member member = model;
        memberEvent.accept(new MemberVisitor() {
            @Override
            public void visit(MemberLockedEvent event) throws MemberNotFoundException {
                if (member.memberId == null) {
                    throw new MemberNotFoundException();
                }
                if (lastProcessedEvent != null && lastProcessedEvent.timestamp < event.timestamp){
                    model.apply(event);
                    try {
                        lockPartnerRequestService.lockPartnerRequestByMemberId(member.memberId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void visit(MemberUnlockedEvent event) throws MemberNotFoundException {
                if (member.memberId == null) {
                    throw new MemberNotFoundException();
                }
                if (lastProcessedEvent != null && lastProcessedEvent.timestamp < event.timestamp){
                    model.apply(event);
                }

            }

            @Override
            public void visit(MemberAddedEvent event) {
                try {
                    member.apply(event, mapper.readValue(event.payload, JsonNode.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return member;
    }

}
