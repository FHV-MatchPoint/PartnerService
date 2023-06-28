package at.fhv.matchpoint.partnerservice.events.court;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;

public class RequestInitiateFailedEvent extends CourtEvent{
    @Override
    public void accept(PartnerRequestCourtVisitor v) throws MongoDBPersistenceError {
        v.visit(this);
    }
}
