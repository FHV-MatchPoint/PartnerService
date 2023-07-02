package at.fhv.matchpoint.partnerservice.utils.exceptions;

public class PartnerRequestNotFoundException extends ResponseException{

    public PartnerRequestNotFoundException() {
        super(404, "Invalid PartnerRequestId");
    }

}
