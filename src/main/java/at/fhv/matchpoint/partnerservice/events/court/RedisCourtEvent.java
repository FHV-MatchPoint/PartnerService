package at.fhv.matchpoint.partnerservice.events.court;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class RedisCourtEvent {

    @JsonProperty("EventId")
    public String eventId;
    public String eventType;
    @JsonProperty("RequestData")
    public JsonNode requestData;
    
}
