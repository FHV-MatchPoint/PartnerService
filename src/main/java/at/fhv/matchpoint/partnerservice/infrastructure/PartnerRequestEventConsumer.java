package at.fhv.matchpoint.partnerservice.infrastructure;

import at.fhv.matchpoint.partnerservice.application.LockPartnerRequestService;
import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import at.fhv.matchpoint.partnerservice.events.ClubLockedEvent;
import at.fhv.matchpoint.partnerservice.events.Event;
import at.fhv.matchpoint.partnerservice.events.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestCancelledEvent;
import at.fhv.matchpoint.partnerservice.events.RequestInitiatedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestUpdatedEvent;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

    // just for testing may not be needed
    public void sendMessage(Event event){

        Map<String, Event> events = new HashMap<>();
        events.put("data", event);

        redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
        System.out.println(redisDataSource.stream(TYPE).xlen(STREAM_KEY));

    }

    // constantly checks if new messages are available to be read and processed
    @Scheduled(every="0s")
    void readMessage(){
        // > reads only messages that have not been read by any other consumer of the group
        List<Map<String, Event>> messages = redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">")
                .stream().map(message -> message.payload()).collect(Collectors.toList());

        for (Map<String,Event> payload : messages) {
            for (String key : payload.keySet()) {
                try {
                    Optional<PartnerRequestReadModel> optPartnerRequest = partnerRequestReadModelRepository.findByIdOptional(payload.get(key).aggregateId);
                    PartnerRequestReadModel partnerRequestReadModel = new PartnerRequestReadModel();
                    if(optPartnerRequest.isPresent()){
                        partnerRequestReadModel = optPartnerRequest.get();    
                    }
                    partnerRequestReadModel = build(partnerRequestReadModel, payload.get(key));
                    redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, key);
                    partnerRequestReadModelRepository.persist(partnerRequestReadModel);
                } catch (Exception e) {
                    LOGGER.info(e.getMessage());
                   
                }
            }
        }
    }


    // when consumer fails, claim the open messages
    // @Scheduled(every="600s")
    // void claimOpenMessages(){
    //     redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600) , "0")
    //             .getMessages().forEach(message -> {
    //                 message.payload().values().stream().forEach(object -> {
    //                     try {
    //                         lockPartnerRequestService.lockPartnerRequestByClubId(object.clubId);
    //                         redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
    //                     } catch (Exception e) {
    //                        LOGGER.info("Not all Request could be cancelled. Message will not be acknowledged");
    //                     }
    //                 });
    //             });
    // }

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
        events.put("data", event);

        redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
        System.out.println(redisDataSource.stream(TYPE).xlen(STREAM_KEY));
    }
   
}
