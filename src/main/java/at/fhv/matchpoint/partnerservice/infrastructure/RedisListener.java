// package at.fhv.matchpoint.partnerservice.infrastructure;

// import java.time.Duration;
// import java.time.LocalTime;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.UUID;

// import at.fhv.matchpoint.partnerservice.event.Event;
// import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;
// import io.quarkus.redis.datasource.RedisDataSource;
// import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
// import io.quarkus.scheduler.Scheduled;
// import jakarta.annotation.PostConstruct;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;

// @ApplicationScoped
// public class RedisListener {

//     @Inject
//     RedisDataSource redisDataSource;

//     final Class<Event> TYPE = Event.class;
//     final String GROUP_NAME = "partnerservice";
//     final String STREAM_KEY = "event";
//     final String CONSUMER = UUID.randomUUID().toString();

//     // create group for horizontal scaling. this way each partner service instance doesnt ready messages multiple times
//     @PostConstruct
//     public void createGroup(){
//         try {
//             redisDataSource.stream(TYPE).xgroupCreate(STREAM_KEY, GROUP_NAME, "$", new XGroupCreateArgs().mkstream());
//         } catch (Exception e) {
//             System.out.println("Group already exists");
//             //TODO delete old consumers
//         }        
//     }

//     // may be not needed
//     public void sendMessage(String message){

//         Map<String, Event> events = new HashMap<>();
//         Event event =  new RequestCreatedEvent();
//         event.aggregateId = message + LocalTime.now().toString();
//         events.put("data", event);

//         redisDataSource.stream(TYPE).xadd(STREAM_KEY, events);
//         System.out.println(redisDataSource.stream(TYPE).xlen(STREAM_KEY));

//     }

//     // constantly checks if new messages are available to be read and processed
//     @Scheduled(every="30s")
//     void readMessage(){
//         // > reads only messages that have not been read by any other consumer of the group
//         redisDataSource.stream(TYPE).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">")
//         .forEach(message -> {
//             message.payload().values().stream().forEach(object -> {
//                 System.out.println("NORMAL: " + object.toString());
//                 redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
//             });
//         });
//     }


//     // when consumer fails, claim the open messages
//     @Scheduled(every="600s")
//     void claimOpenMessages(){
//         redisDataSource.stream(TYPE).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600), "0")
//         .getMessages().forEach(message -> {
//             message.payload().values().stream().forEach(object -> {
//                 System.out.println("CLAIMED: " + object.toString());
//                 redisDataSource.stream(TYPE).xack(STREAM_KEY, GROUP_NAME, message.id());
//             });
//         });
//     }
    
// }

