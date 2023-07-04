package at.fhv.matchpoint.partnerservice.events.court;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;


public abstract class CourtEvent implements Comparable<CourtEvent> {

    public String eventId;
    public String aggregateId;
    public String partnerId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    @Override
    public int compareTo(CourtEvent e) {
        return eventId.compareTo(e.eventId);
    }

    public abstract void accept(PartnerRequestCourtVisitor v) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException;

    public String getPartnerRequestId() {
        return aggregateId;
    }    

    public String getEventId() {
        return eventId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public static CourtEvent createFrom(RedisCourtEvent redisCourtEvent) throws JsonMappingException, JsonProcessingException {
        switch (redisCourtEvent.eventType) {
            case "Domain.Events.PartnerServiceEvents.RequestInitiateSucceededEvent":                
                return new RequestInitiateSucceededEvent(redisCourtEvent);
            case "Domain.Events.PartnerServiceEvents.RequestInitiateFailedEvent":
                return new RequestInitiateFailedEvent(redisCourtEvent);
            case "Domain.Events.PartnerServiceEvents.SessionCreateSucceededEvent":
                ObjectMapper mapper = new ObjectMapper();
                JsonNode data = mapper.readTree(redisCourtEvent.requestData.asText());
                String partnerId = data.get("partnerId").asText();
                return new SessionCreateSucceededEvent(partnerId, redisCourtEvent);
            case "Domain.Events.PartnerServiceEvents.SessionCreateFailedEvent":
                return new SessionCreateFailedEvent(redisCourtEvent);
            default:
                return null;
        }
    }

    void parseJson(RedisCourtEvent redisCourtEvent) throws JsonMappingException, JsonProcessingException{
        this.eventId = redisCourtEvent.eventId;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(redisCourtEvent.requestData.asText());
        this.aggregateId = data.get("aggregateId").asText();
        Instant instant = Instant.ofEpochMilli(Long.parseLong(data.get("date").get("$date").asText()));
        this.date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate();
        instant = Instant.ofEpochMilli(Long.parseLong(data.get("startTime").get("$date").asText()));
        this.startTime = LocalTime.ofInstant(instant, ZoneOffset.UTC);
        instant = Instant.ofEpochMilli(Long.parseLong(data.get("endTime").get("$date").asText()));
        this.endTime = LocalTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
