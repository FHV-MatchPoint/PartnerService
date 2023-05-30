package at.fhv.matchpoint.partnerservice.infrastructure.consumer;

import at.fhv.matchpoint.partnerservice.events.request.PartnerRequestEvent;
import at.fhv.matchpoint.partnerservice.infrastructure.PartnerRequestEventHandler;
import at.fhv.matchpoint.partnerservice.utils.LocalDateDeserializer;
import at.fhv.matchpoint.partnerservice.utils.LocalDateTimeDeserializer;
import at.fhv.matchpoint.partnerservice.utils.LocalTimeDeserializer;
import at.fhv.matchpoint.partnerservice.utils.ObjectIdDeserializer;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.stream.StreamMessage;
import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ApplicationScoped
public class PartnerRequestEventConsumer {

    @Inject
    RedisDataSource redisDataSource;

    @Inject
    PartnerRequestEventHandler partnerRequestEventHandler;

    @Inject
    ObjectMapper mapper;

    private static final Logger LOGGER = Logger.getLogger(PartnerRequestEventConsumer.class);

    final String GROUP_NAME = "partnerService";
    final String STREAM_KEY = "partnerservice.event.Event";
    final String CONSUMER = UUID.randomUUID().toString();
    final Class<JsonNode> TYPE = JsonNode.class;
    final String PAYLOAD_KEY = "value";

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
    @Scheduled(every="0s")
    void readMessage(){
        // > reads only messages that have not been read by any other consumer of the group
        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">");

        for (StreamMessage<String, String, JsonNode> message : messages) {
            Map<String,JsonNode> payload = message.payload();
            try {
                PartnerRequestEvent event = mapper.readValue(payload.get("value").get("payload").get("after").asText(), PartnerRequestEvent.class);
                partnerRequestEventHandler.handleEvent(event);
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());                
            }
        }
    }


    // when consumer fails, claim the open messages
    @Scheduled(every="600s")
    void claimOpenMessages(){
        List<StreamMessage<String, String, JsonNode>> messages = redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600) , "0").getMessages();

        for (StreamMessage<String, String, JsonNode> message : messages) {
            Map<String,JsonNode> payload = message.payload();
            try {
                PartnerRequestEvent event = mapper.readValue(payload.get("value").get("payload").get("after").asText(), PartnerRequestEvent.class);
                partnerRequestEventHandler.handleEvent(event);
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
            } catch (Exception e) {
                LOGGER.info(e.getMessage());                
            }
        }
    }

}
