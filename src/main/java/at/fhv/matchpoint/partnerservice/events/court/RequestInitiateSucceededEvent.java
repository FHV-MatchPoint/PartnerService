package at.fhv.matchpoint.partnerservice.events.court;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;

public class RequestInitiateSucceededEvent extends CourtEvent {
    @Override
    public void accept(PartnerRequestCourtVisitor v) {
        v.visit(this);
    }
}
