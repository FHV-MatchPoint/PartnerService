package at.fhv.matchpoint.partnerservice.utils.exceptions;

public class MemberNotAuthorizedException extends ResponseException {

    public MemberNotAuthorizedException(String message) {
        super(401, message);
    }
}
