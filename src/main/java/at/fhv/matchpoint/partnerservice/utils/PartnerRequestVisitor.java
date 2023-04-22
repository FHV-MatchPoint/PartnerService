package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.events.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestCancelledEvent;
import at.fhv.matchpoint.partnerservice.events.RequestInitiatedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestUpdatedEvent;

public interface PartnerRequestVisitor {

    void visit(RequestAcceptedEvent event);
    void visit(RequestInitiatedEvent event);
    void visit(RequestUpdatedEvent event);
    void visit(RequestCancelledEvent event);
}
