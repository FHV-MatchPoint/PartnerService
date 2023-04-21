// package at.fhv.matchpoint.partnerservice.infrastructure;

// import java.time.Duration;
// import java.util.UUID;

// import io.quarkus.redis.datasource.RedisDataSource;
// import io.quarkus.redis.datasource.stream.XGroupCreateArgs;
// import io.quarkus.scheduler.Scheduled;
// import jakarta.annotation.PostConstruct;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;

// @ApplicationScoped
// public abstract class RedisListener<T> {

//     @Inject
//     RedisDataSource redisDataSource;

//     final String GROUP_NAME = "partnerservice";
//     final String STREAM_KEY = "event";
//     final String CONSUMER = UUID.randomUUID().toString();

//     abstract Class<T> type();

//     // create group for horizontal scaling. this way each partner service instance doesnt ready messages multiple times
//     @PostConstruct
//     public void createGroup(){
//         try {
//             redisDataSource.stream(type()).xgroupCreate(STREAM_KEY, GROUP_NAME, "$", new XGroupCreateArgs().mkstream());
//         } catch (Exception e) {
//             System.out.println("Group already exists");
//             //TODO delete old consumers
//         }        
//     }

//     // constantly checks if new messages are available to be read and processed
//     @Scheduled(every="30s")
//     void readMessage(){
//         // > reads only messages that have not been read by any other consumer of the group
//         redisDataSource.stream(type()).xreadgroup(GROUP_NAME, CONSUMER, STREAM_KEY, ">")
//         .forEach(message -> {
//             message.payload().values().stream().forEach(object -> {
//                 System.out.println("NORMAL: " + object.toString());
//                 redisDataSource.stream(type()).xack(STREAM_KEY, GROUP_NAME, message.id());
//             });
//         });
//     }


//     // when consumer fails, claim the open messages
//     @Scheduled(every="600s")
//     void claimOpenMessages(){
//         redisDataSource.stream(type()).xautoclaim(STREAM_KEY, GROUP_NAME, CONSUMER, Duration.ofSeconds(600), "0")
//         .getMessages().forEach(message -> {
//             message.payload().values().stream().forEach(object -> {
//                 System.out.println("CLAIMED: " + object.toString());
//                 redisDataSource.stream(type()).xack(STREAM_KEY, GROUP_NAME, message.id());
//             });
//         });
//     }
    
// }

