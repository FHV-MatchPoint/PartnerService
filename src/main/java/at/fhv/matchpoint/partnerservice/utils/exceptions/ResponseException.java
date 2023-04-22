package at.fhv.matchpoint.partnerservice.utils.exceptions;

public abstract class ResponseException extends Exception {

    private Integer statusCode;
    private String responseMessage;

    public ResponseException(Integer statusCode, String message){
        this.statusCode = statusCode;
        this.responseMessage = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
