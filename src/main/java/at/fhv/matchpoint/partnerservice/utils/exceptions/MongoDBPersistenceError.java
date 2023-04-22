package at.fhv.matchpoint.partnerservice.utils.exceptions;

public class MongoDBPersistenceError extends ResponseException {
    public MongoDBPersistenceError() {
        super(500, "There was an error with the server. The action could not be processed");
    }
}
