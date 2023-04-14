package at.fhv.matchpoint.partnerservice.event;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

@ApplicationScoped
public class EventRepository implements PanacheMongoRepository<Event> {
    
}
