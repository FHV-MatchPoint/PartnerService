package at.fhv.matchpoint.partnerservice.events.court;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;

public class RequestInitiateSucceededEvent extends CourtEvent {
    public RequestInitiateSucceededEvent(RedisCourtEvent redisCourtEvent) throws JsonMappingException, JsonProcessingException {
        parseJson(redisCourtEvent);
    }

    @Override
    public void accept(PartnerRequestCourtVisitor v) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException {
        v.visit(this);
    }
}
