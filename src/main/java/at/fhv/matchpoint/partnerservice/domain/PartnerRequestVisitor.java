package at.fhv.matchpoint.partnerservice.domain;

import at.fhv.matchpoint.partnerservice.event.Event;
import at.fhv.matchpoint.partnerservice.event.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;

public interface PartnerRequestVisitor {

    void visit(RequestAcceptedEvent event);
    void visit(RequestCreatedEvent event);
}
