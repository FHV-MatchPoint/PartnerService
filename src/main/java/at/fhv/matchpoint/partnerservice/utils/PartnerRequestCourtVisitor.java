package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateSucceededEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateSucceededEvent;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;

public interface PartnerRequestCourtVisitor {

    void visit(RequestInitiateFailedEvent event) throws MongoDBPersistenceError;
    void visit(RequestInitiateSucceededEvent event) throws MongoDBPersistenceError, DateTimeFormatException;
    void visit(SessionCreateSucceededEvent event) throws MongoDBPersistenceError, DateTimeFormatException;
    void visit(SessionCreateFailedEvent event) throws MongoDBPersistenceError;
}
