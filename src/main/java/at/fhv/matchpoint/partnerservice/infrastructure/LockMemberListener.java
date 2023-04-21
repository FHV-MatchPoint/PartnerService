// package at.fhv.matchpoint.partnerservice.infrastructure;

// import java.time.LocalTime;
// import java.util.HashMap;
// import java.util.Map;

// import at.fhv.matchpoint.partnerservice.event.Event;
// import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;

// public class LockMemberListener extends RedisListener<Event> {

//     @Override
//     Class<Event> type() {
//         return Event.class;
//     }

//     // just for testing may not be needed
//     public void sendMessage(String message){

//         Map<String, Event> events = new HashMap<>();
//         Event event =  new RequestCreatedEvent();
//         event.aggregateId = message + LocalTime.now().toString();
//         events.put("data", event);

//         redisDataSource.stream(type()).xadd(STREAM_KEY, events);
//         System.out.println(redisDataSource.stream(type()).xlen(STREAM_KEY));

//     }
    
// }
