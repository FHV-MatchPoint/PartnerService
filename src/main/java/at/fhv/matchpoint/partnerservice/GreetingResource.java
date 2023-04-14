package at.fhv.matchpoint.partnerservice;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.fhv.matchpoint.partnerservice.event.Event;
import at.fhv.matchpoint.partnerservice.event.EventRepository;
import at.fhv.matchpoint.partnerservice.event.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;

@Path("/")
public class GreetingResource {

    @Inject
    EventRepository eventRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Event ev1 = new RequestAcceptedEvent();
        Event ev2 = new RequestCreatedEvent();
        eventRepository.persist(ev1);
        eventRepository.persist(ev2);
        System.out.println(eventRepository.count());
        return "Hello from RESTEasy Reactive";
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> events(){
        return eventRepository.listAll();
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
        RequestCreatedEvent ev = eventRepository.find( "ownerId", "Markomannen").firstResult();
        return ev.ownerId;
    }
}