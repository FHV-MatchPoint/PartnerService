package at.fhv.matchpoint.partnerservice.infrastructure;

import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import at.fhv.matchpoint.partnerservice.events.Event;
import at.fhv.matchpoint.partnerservice.events.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestCancelledEvent;
import at.fhv.matchpoint.partnerservice.events.RequestInitiatedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestUpdatedEvent;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.stream.StreamMessage;
import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PartnerRequestEventConsumer {

    @Inject
    RedisDataSource redisDataSource;

    @Inject
    PartnerRequestReadModelRepository partnerRequestReadModelRepository;

    private static final Logger LOGGER = Logger.getLogger(PartnerRequestEventConsumer.class);

    final String GROUP_NAME = "partnerService";
    final String STREAM_KEY = "requestEvent";
    final String CONSUMER = UUID.randomUUID().toString();
    final Class<Event> TYPE = Event.class;
    final String PAYLOAD_KEY = "data";

    // create group for horizontal scaling. this way each partner service instance doesnt ready messages multiple times
    @PostConstruct
    public void createGroup(){
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
        List<StreamMessage<String, String, Event>> messages = redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">");

        for (StreamMessage<String, String, Event> message : messages) {
            Map<String,Event> payload = message.payload();
            try {
                Optional<PartnerRequestReadModel> optPartnerRequest = partnerRequestReadModelRepository.findByIdOptional(payload.get(PAYLOAD_KEY).aggregateId);
                PartnerRequestReadModel partnerRequestReadModel = new PartnerRequestReadModel();
                if(optPartnerRequest.isPresent()){
                    partnerRequestReadModel = optPartnerRequest.get();    
                }
                partnerRequestReadModel = build(partnerRequestReadModel, payload.get(PAYLOAD_KEY));
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                partnerRequestReadModelRepository.persist(partnerRequestReadModel);
            } catch (Exception e) {
                LOGGER.info(e.getMessage());                
            }
        }
    }


    // when consumer fails, claim the open messages
    @Scheduled(every="600s")
    void claimOpenMessages(){
        List<StreamMessage<String, String, Event>> messages = redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600) , "0").getMessages();

        for (StreamMessage<String, String, Event> message : messages) {
            Map<String,Event> payload = message.payload();
            try {
                Optional<PartnerRequestReadModel> optPartnerRequest = partnerRequestReadModelRepository.findByIdOptional(payload.get(PAYLOAD_KEY).aggregateId);
                PartnerRequestReadModel partnerRequestReadModel = new PartnerRequestReadModel();
                if(optPartnerRequest.isPresent()){
                    partnerRequestReadModel = optPartnerRequest.get();    
                }
                partnerRequestReadModel = build(partnerRequestReadModel, payload.get(PAYLOAD_KEY));
                redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                partnerRequestReadModelRepository.persist(partnerRequestReadModel);
            } catch (Exception e) {
                LOGGER.info(e.getMessage());                
            }
        }
    }

    private PartnerRequestReadModel build(PartnerRequestReadModel model, Event event){
        PartnerRequestReadModel requestReadModel = model;
        event.accept(new PartnerRequestVisitor() {
    
            @Override
            public void visit(RequestAcceptedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestInitiatedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestUpdatedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestCancelledEvent event) {
                requestReadModel.apply(event);
            }
        });
        return requestReadModel;
    }

    public void send(RequestInitiatedEvent event) {
        Map<String, Event> events = new HashMap<>();
        events.put(PAYLOAD_KEY, event);

        redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
    }

    public void send(RequestAcceptedEvent event) {
        Map<String, Event> events = new HashMap<>();
        events.put(PAYLOAD_KEY, event);

        redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
    }

    public void send(RequestUpdatedEvent event) {
        Map<String, Event> events = new HashMap<>();
        events.put(PAYLOAD_KEY, event);

        redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
    }

    public void send(RequestCancelledEvent event) {
        Map<String, Event> events = new HashMap<>();
        events.put(PAYLOAD_KEY, event);

        redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
    }
   
}
