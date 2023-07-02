package at.fhv.matchpoint.partnerservice.events;

import java.time.LocalDateTime;

import at.fhv.matchpoint.partnerservice.events.request.PartnerRequestEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PartnerRequestEventTracking {

    @Id
    public String eventId;
    public LocalDateTime createdAt;
    public AggregateType aggregateType;
    public String aggregateId;

    public PartnerRequestEventTracking(){}

    public PartnerRequestEventTracking(PartnerRequestEvent event) {
        this.aggregateId = event.aggregateId;
        this.eventId = event.eventId.toString();
        this.createdAt = event.createdAt;
        this.aggregateType = event.aggregateType;
    }
}
