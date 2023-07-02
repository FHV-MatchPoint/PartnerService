package at.fhv.matchpoint.partnerservice.events.member;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "event_type",
    visible = true
)
@JsonSubTypes({
    @Type(value = MemberAddedEvent.class, name = "MemberAddedEvent"),
    @Type(value = MemberLockedEvent.class, name = "MemberLockedEvent"),
    @Type(value = MemberUnlockedEvent.class, name = "MemberUnlockedEvent")
})
public abstract class MemberEvent implements Comparable<MemberEvent> {

    @Id
    @JsonProperty("event_id")
    public String eventId;
    @JsonProperty("timestamp")
    public Long timestamp;
    @JsonProperty("entity_type")
    public String entity_type;
    @JsonProperty("entity_id")
    public String entity_id;
    @JsonProperty("payload")
    public String payload;

    public MemberEvent(){}

    public MemberEvent(String entity_type, String entity_id){
        this.entity_type = entity_type;
        this.entity_id = entity_id;
    }

    public abstract void accept(MemberVisitor v) throws MemberNotFoundException;

    @Override
    public int compareTo(MemberEvent e) {
        return timestamp.compareTo(e.timestamp);
    }
    
}
