 package at.fhv.matchpoint.partnerservice.infrastructure.consumer;

 import at.fhv.matchpoint.partnerservice.application.LockPartnerRequestService;
 import at.fhv.matchpoint.partnerservice.events.ClubLockedEvent;
 import io.quarkus.redis.datasource.RedisDataSource;
 import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
 import io.quarkus.scheduler.Scheduled;
 import jakarta.annotation.PostConstruct;
 import jakarta.enterprise.context.ApplicationScoped;
 import jakarta.inject.Inject;

 import java.time.Duration;
 import java.util.HashMap;
 import java.util.Map;
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
     final String STREAM_KEY = "lockClub";
     final String CONSUMER = UUID.randomUUID().toString();
     final Class<ClubLockedEvent> TYPE = ClubLockedEvent.class;

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
     public void sendMessage(String clubId){

         Map<String, ClubLockedEvent> events = new HashMap<>();
         ClubLockedEvent event =  new ClubLockedEvent();
         event.clubId = clubId;
         events.put("data", event);

         redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
         System.out.println(redisDataSource.stream(TYPE).xlen(STREAM_KEY));

     }

     // constantly checks if new messages are available to be read and processed
     @Scheduled(every="0s")
     void readMessage(){
         // > reads only messages that have not been read by any other consumer of the group
         redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">")
                 .forEach(message -> {
                     message.payload().values().stream().forEach(object -> {
                         try {
                             lockPartnerRequestService.lockPartnerRequestByClubId(object.clubId);
                             redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                         } catch (Exception e) {
                            LOGGER.info("Not all Request could be cancelled. Message will not be acknowledged");
                            
                         }
                     });
                 });
     }


     // when consumer fails, claim the open messages
     @Scheduled(every="600s")
     void claimOpenMessages(){
         redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600) , "0")
                 .getMessages().forEach(message -> {
                     message.payload().values().stream().forEach(object -> {
                         try {
                             lockPartnerRequestService.lockPartnerRequestByClubId(object.clubId);
                             redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                         } catch (Exception e) {
                            LOGGER.info("Not all Request could be cancelled. Message will not be acknowledged");
                         }
                     });
                 });
     }
    
 }
