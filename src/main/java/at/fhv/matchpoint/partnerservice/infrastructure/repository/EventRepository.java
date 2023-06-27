package at.fhv.matchpoint.partnerservice.infrastructure.repository;

import at.fhv.matchpoint.partnerservice.events.request.PartnerRequestEvent;
import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

@ApplicationScoped
public class EventRepository implements PanacheMongoRepository<PartnerRequestEvent> {
    
}
