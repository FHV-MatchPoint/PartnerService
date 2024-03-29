 package at.fhv.matchpoint.partnerservice.infrastructure.consumer;

 import at.fhv.matchpoint.partnerservice.application.LockPartnerRequestService;
 import at.fhv.matchpoint.partnerservice.events.club.ClubLockedEvent;
 import io.quarkus.redis.datasource.RedisDataSource;
 import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
 import io.quarkus.scheduler.Scheduled;
 import jakarta.annotation.PostConstruct;
 import jakarta.enterprise.context.ApplicationScoped;
 import jakarta.inject.Inject;

 import java.time.Duration;
 import java.util.UUID;

import org.jboss.logging.Logger;

 @ApplicationScoped
 public class LockClubConsumer {

     @Inject
     RedisDataSource redisDataSource;

     @Inject
     LockPartnerRequestService lockPartnerRequestService;

     private static final Logger LOGGER = Logger.getLogger(LockClubConsumer.class);

     final String GROUP_NAME = "partnerService";
     final String STREAM_KEY = "club";
     final String CONSUMER = UUID.randomUUID().toString();
     final Class<String> TYPE = String.class;

     // create group for horizontal scaling. this way each partner service instance doesnt ready messages multiple times
     @PostConstruct
     public void createGroup(){
         try {
             redisDataSource.stream(TYPE).xgroupCreate(STREAM_KEY, GROUP_NAME, "0", new XGroupCreateArgs().mkstream());
         } catch (Exception e) {
             LOGGER.info("Group already exists");
         }
     }

     // constantly checks if new messages are available to be read and processed
     @Scheduled(every="0s")
     void readMessage(){
         // > reads only messages that have not been read by any other consumer of the group
         redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">")
                 .forEach(message -> {
                    ClubLockedEvent event = new ClubLockedEvent();
                    event.aggregateId = message.payload().get("aggregateId");
                    event.eventType = message.payload().get("eventType");
                    event.id = message.payload().get("id");
                    if(event.eventType.equals("ClubLockedEvent")){
                         try {
                             lockPartnerRequestService.lockPartnerRequestByClubId(event.aggregateId);
                             redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                         } catch (Exception e) {
                            LOGGER.info("Not all Request could be cancelled. Message will not be acknowledged");

                         }
                    }
                 });
     }


     // when consumer fails, claim the open messages
     @Scheduled(every="600s")
     void claimOpenMessages(){
         redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600) , "0")
                 .getMessages().forEach(message -> {
                    ClubLockedEvent event = new ClubLockedEvent();
                    event.aggregateId = message.payload().get("aggregateId");
                    event.eventType = message.payload().get("eventType");
                    event.id = message.payload().get("id");
                    if(event.eventType.equals("ClubLockedEvent")){
                         try {
                             lockPartnerRequestService.lockPartnerRequestByClubId(event.aggregateId);
                             redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                         } catch (Exception e) {
                            LOGGER.info("Not all Request could be cancelled. Message will not be acknowledged");

                         }
                    }
                 });
     }
    
 }
