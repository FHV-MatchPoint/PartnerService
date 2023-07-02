package at.fhv.matchpoint.partnerservice.events.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubLockedEvent {

    @JsonProperty("_class")
    public String whatTheFuck;
    public String id;
    public String aggregateId;
    public String aggregateType;
    public String eventType;

}
