package at.fhv.matchpoint.partnerservice.events;

import java.time.LocalDateTime;

import at.fhv.matchpoint.partnerservice.utils.ObjectIdDeserializer;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@JsonTypeInfo(
		use = JsonTypeInfo.Id.CLASS,
		include = JsonTypeInfo.As.PROPERTY,
		property = "_t"
)
@BsonDiscriminator
public abstract class Event implements Comparable<Event> {

    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId eventId;
    public LocalDateTime createdAt;
    public AggregateType aggregateType;
    public String aggregateId;

    public Event(){}

    public Event(AggregateType aggregateType, String aggregateId){
        this.createdAt = LocalDateTime.now();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
    }

    public abstract void accept(PartnerRequestVisitor v);

//    public abstract void accept(MemberVisitor v);

    @Override
    public int compareTo(Event e) {
        return createdAt.compareTo(e.createdAt);
    }
    
}
