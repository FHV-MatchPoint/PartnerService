 package at.fhv.matchpoint.partnerservice.infrastructure;

 import java.time.Duration;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.UUID;

import org.jboss.logging.Logger;

import at.fhv.matchpoint.partnerservice.application.LockPartnerRequestService;
 import at.fhv.matchpoint.partnerservice.events.MemberLockedEvent;
 import io.quarkus.redis.datasource.RedisDataSource;
 import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
 import io.quarkus.scheduler.Scheduled;
 import jakarta.annotation.PostConstruct;
 import jakarta.enterprise.context.ApplicationScoped;
 import jakarta.inject.Inject;

 @ApplicationScoped
 public class LockMemberConsumer {

     @Inject
     RedisDataSource redisDataSource;

     @Inject
     LockPartnerRequestService lockPartnerRequestService;

     private static final Logger LOGGER = Logger.getLogger(LockMemberConsumer.class);

     final String GROUP_NAME = "partnerService";
     final String STREAM_KEY = "lockMember";
     final String CONSUMER = UUID.randomUUID().toString();
     final Class<MemberLockedEvent> TYPE = MemberLockedEvent.class;

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
     public void sendMessage(String memberId){

         Map<String, MemberLockedEvent> events = new HashMap<>();
         MemberLockedEvent event =  new MemberLockedEvent();
         event.memberId = memberId;
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
                             lockPartnerRequestService.lockPartnerRequestByMemberId(object.memberId);
                             redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                             System.out.println(object.memberId);
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
                             lockPartnerRequestService.lockPartnerRequestByMemberId(object.memberId);
                             redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
                         } catch (Exception e) {
                            LOGGER.info("Not all Request could be cancelled. Message will not be acknowledged");
                         }
                     });
                 });
     }
    
 }
