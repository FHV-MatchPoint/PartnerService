package at.fhv.matchpoint.partnerservice.events.court;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;

public class SessionCreateSucceededEvent extends CourtEvent{
    @Override
    public void accept(PartnerRequestCourtVisitor v) throws MongoDBPersistenceError, DateTimeFormatException {
        v.visit(this);
    }

    public String getPartnerId() {
        return null;
    }

    public String getStartTime() {
        return null;
    }

    public String getEndTime() {
        return null;
    }
}
