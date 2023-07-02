package at.fhv.matchpoint.partnerservice.events.court;

import at.fhv.matchpoint.partnerservice.utils.ObjectIdDeserializer;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public abstract class CourtEvent implements Comparable<CourtEvent> {

    @JsonProperty("event_id") // maybe TODO
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public String eventId;
    public String aggregateId;

    @Override
    public int compareTo(CourtEvent e) {
        return eventId.compareTo(e.eventId);
    }

    public abstract void accept(PartnerRequestCourtVisitor v) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException;

    public String getPartnerRequestId() {
        return aggregateId;
    }
}
