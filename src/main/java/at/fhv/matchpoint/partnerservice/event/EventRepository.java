package at.fhv.matchpoint.partnerservice.event;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

@ApplicationScoped
public class EventRepository implements PanacheMongoRepository<Event> {
    
}
