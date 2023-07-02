package at.fhv.matchpoint.partnerservice.events.court;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;

public class RequestInitiateSucceededEvent extends CourtEvent {
    @Override
    public void accept(PartnerRequestCourtVisitor v) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException {
        v.visit(this);
    }

    public String getMemberId() {
        return null;
    }

    public String getClubId() {
        return null;
    }

    public String getDate() {
        return null;
    }

    public String getStartTime() {
        return null;
    }

    public String getEndTime() {
        return null;
    }
}
