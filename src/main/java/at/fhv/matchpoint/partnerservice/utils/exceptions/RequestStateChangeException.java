package at.fhv.matchpoint.partnerservice.utils.exceptions;

public abstract class RequestStateChangeException extends ResponseException {

    public RequestStateChangeException() {
        super(412, "The request could not be process as the PartnerRequest no longer fulfills the necessary preconditions or was cancelled.");
    }
}
