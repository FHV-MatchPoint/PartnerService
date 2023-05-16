package at.fhv.matchpoint.partnerservice.events;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.ObjectIdDeserializer;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public abstract class MemberEvent implements Comparable<MemberEvent> {

    @JsonProperty("event_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId eventId;
    public LocalDateTime createdAt;
    public AggregateType aggregateType;
    public String aggregateId;
    public String payload;

    public MemberEvent(){}

    public MemberEvent(AggregateType aggregateType, String aggregateId){
        this.createdAt = LocalDateTime.now();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
    }

    public abstract void accept(MemberVisitor v) throws MemberNotFoundException;

    @Override
    public int compareTo(MemberEvent e) {
        return createdAt.compareTo(e.createdAt);
    }
    
}
