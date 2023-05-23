package at.fhv.matchpoint.partnerservice.events;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.ObjectIdDeserializer;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

public abstract class MemberEvent implements Comparable<MemberEvent> {

    @JsonProperty("event_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public String eventId;
    public LocalDateTime timestamp;
    public AggregateType entity_type;
    public String entity_id;
    public String payload;

    public MemberEvent(){}

    public MemberEvent(AggregateType entity_type, String entity_id){
        this.timestamp = LocalDateTime.now();
        this.entity_type = entity_type;
        this.entity_id = entity_id;
    }

    public abstract void accept(MemberVisitor v) throws MemberNotFoundException;

    @Override
    public int compareTo(MemberEvent e) {
        return timestamp.compareTo(e.timestamp);
    }
    
}
