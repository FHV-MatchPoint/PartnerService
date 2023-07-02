package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateSucceededEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateSucceededEvent;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;

public interface PartnerRequestCourtVisitor {

    void visit(RequestInitiateFailedEvent event) throws MongoDBPersistenceError, PartnerRequestAlreadyCancelledException;
    void visit(RequestInitiateSucceededEvent event) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException;
    void visit(SessionCreateSucceededEvent event) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException;
    void visit(SessionCreateFailedEvent event) throws MongoDBPersistenceError, PartnerRequestAlreadyCancelledException;
}
