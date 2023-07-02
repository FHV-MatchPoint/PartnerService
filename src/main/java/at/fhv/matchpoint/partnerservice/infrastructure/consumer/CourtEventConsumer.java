package at.fhv.matchpoint.partnerservice.infrastructure.consumer;

import at.fhv.matchpoint.partnerservice.events.court.CourtEvent;
import at.fhv.matchpoint.partnerservice.infrastructure.CourtEventHandler;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.stream.StreamMessage;
import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.*;

@ApplicationScoped
public class CourtEventConsumer {

    @Inject
    RedisDataSource redisDataSource;

    @Inject
    CourtEventHandler courtEventHandler;

    @Inject
    ObjectMapper mapper;

    private static final Logger LOGGER = Logger.getLogger(CourtEventConsumer.class);
    final String GROUP_NAME = "partnerService";
    final String STREAM_KEY = "courtservice.event.Event";
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

    // constantly checks if new messages are available to be read and processed
    @Scheduled(every = "0s")
    void readMessage() {
        // > reads only messages that have not been read by any other consumer of the group
        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">");

        for (StreamMessage<String, String, JsonNode> message : messages) {
            Map<String, JsonNode> payload = message.payload();
            try {
                CourtEvent courtEvent = mapper.readValue(payload.get(PAYLOAD_KEY).get("payload").get("after").asText(), CourtEvent.class);
                courtEventHandler.handleEvent(courtEvent);
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (PartnerRequestAlreadyCancelledException e) {
                // when already cancelled event can be ignored and acknowledged
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }

    }

    // when consumer fails, claim the open messages
    @Scheduled(every = "600s")
    void claimOpenMessages() {
        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600) , "0").getMessages();

        for (StreamMessage<String, String, JsonNode> message : messages) {
            Map<String,JsonNode> payload = message.payload();
            try {
                CourtEvent event = mapper.readValue(payload.get(PAYLOAD_KEY).get("payload").get("after").asText(), CourtEvent.class);
                courtEventHandler.handleEvent(event);
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (PartnerRequestAlreadyCancelledException e) {
                // when already cancelled event can be ignored and acknowledged
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
    }
}
