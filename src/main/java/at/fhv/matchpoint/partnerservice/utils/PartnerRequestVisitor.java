package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.events.request.*;

public interface PartnerRequestVisitor {

    void visit(RequestAcceptedEvent event);
    void visit(RequestInitiatedEvent event);
    void visit(RequestUpdatedEvent event);
    void visit(RequestCancelledEvent event);
    void visit(RequestOpenedEvent event);
    void visit(RequestAcceptPendingEvent event);
    void visit(RequestRevertPendingEvent event);
}
