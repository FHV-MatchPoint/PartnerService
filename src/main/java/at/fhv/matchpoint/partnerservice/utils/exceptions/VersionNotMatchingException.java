package at.fhv.matchpoint.partnerservice.utils.exceptions;

public class VersionNotMatchingException extends ResponseException {

    public VersionNotMatchingException() {
        super(400, "Tried to access an old version");
    }

}
