package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateSucceededEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateSucceededEvent;

public interface PartnerRequestCourtVisitor {

    void visit(RequestInitiateFailedEvent event);
    void visit(RequestInitiateSucceededEvent event);
    void visit(SessionCreateSucceededEvent event);
    void visit(SessionCreateFailedEvent event);
}
