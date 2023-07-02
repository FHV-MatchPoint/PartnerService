package at.fhv.matchpoint.partnerservice.events.court;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;

public class SessionCreateFailedEvent extends CourtEvent{
    @Override
    public void accept(PartnerRequestCourtVisitor v) throws MongoDBPersistenceError, PartnerRequestAlreadyCancelledException {
        v.visit(this);
    }
}
