package at.fhv.matchpoint.partnerservice.infrastructure;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import at.fhv.matchpoint.partnerservice.events.*;
import at.fhv.matchpoint.partnerservice.utils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.stream.StreamMessage;
import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MemberEventConsumer {

    @Inject
    RedisDataSource redisDataSource;

    @Inject
    ObjectMapper mapper;

    @Inject
    MemberRepository memberRepository;

    private static final Logger LOGGER = Logger.getLogger(MemberEventConsumer.class);

    final String GROUP_NAME = "partnerService";
    final String STREAM_KEY = "member.event.Event"; //TODO find out
    final String CONSUMER = UUID.randomUUID().toString();
    final Class<JsonNode> TYPE = JsonNode.class;
    final String PAYLOAD_KEY = "value"; //TODO find out

    // create group for horizontal scaling. this way each partner service instance doesnt ready messages multiple times
    @PostConstruct
    public void createGroup(){
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
        module.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
        try {
            redisDataSource.stream(TYPE).xgroupCreate(STREAM_KEY, GROUP_NAME, "$", new XGroupCreateArgs().mkstream());
        } catch (Exception e) {
            LOGGER.info("Group already exists");
            //TODO delete old consumers
        }
    }

    // constantly checks if new messages are available to be read and processed
//    @Scheduled(every="0s")
//    void readMessage(){
//        // > reads only messages that have not been read by any other consumer of the group
//        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">");
//
//        for (StreamMessage<String, String, JsonNode> message : messages) {
//            Map<String,JsonNode> payload = message.payload();
//            try {
//                Event event = mapper.readValue(payload.get("value").get("payload").get("after").asText(), Event.class);
//                System.out.println(event);
//                Optional<Member> optMember = memberRepository.findByIdOptional(event.aggregateId);
//                Member member = new Member();
//                if(optMember.isPresent()){
//                    member = optMember.get();
//                }
//                partnerRequestReadModel = build(partnerRequestReadModel, event);
//                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
//                partnerRequestReadModelRepository.persistAndFlush(partnerRequestReadModel);
//            } catch (Exception e) {
//                LOGGER.info(e.getMessage());
//            }
//        }
//    }


    // when consumer fails, claim the open messages
//    @Scheduled(every="600s")
//    void claimOpenMessages(){
//        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600) , "0").getMessages();
//
//        for (StreamMessage<String, String, JsonNode> message : messages) {
//            Map<String,JsonNode> payload = message.payload();
//            try {
//                Event event = mapper.readValue(payload.get("value").get("payload").get("after").asText(), Event.class);
//                Optional<PartnerRequestReadModel> optPartnerRequest = partnerRequestReadModelRepository.findByIdOptional(event.aggregateId);
//                PartnerRequestReadModel partnerRequestReadModel = new PartnerRequestReadModel();
//                if(optPartnerRequest.isPresent()){
//                    partnerRequestReadModel = optPartnerRequest.get();
//                }
//                partnerRequestReadModel = build(partnerRequestReadModel, event);
//                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
//                partnerRequestReadModelRepository.persist(partnerRequestReadModel);
//            } catch (Exception e) {
//                LOGGER.info(e.getMessage());
//            }
//        }
//    }

//    private Member build(Member model, Event event){
//        Member member = model;
//        event.accept(new PartnerRequestVisitor() {
//
//            @Override
//            public void visit(RequestAcceptedEvent event) {
//                member.apply(event);
//            }
//
//            @Override
//            public void visit(RequestInitiatedEvent event) {
//                requestReadModel.apply(event);
//            }
//
//            @Override
//            public void visit(RequestUpdatedEvent event) {
//                requestReadModel.apply(event);
//            }
//
//            @Override
//            public void visit(RequestCancelledEvent event) {
//                requestReadModel.apply(event);
//            }
//        });
//        return requestReadModel;
//    }

}
