package at.fhv.matchpoint.partnerservice.events;

import java.time.LocalDateTime;

import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@JsonTypeInfo(
		use = JsonTypeInfo.Id.CLASS,
		include = JsonTypeInfo.As.PROPERTY,
		property = "event_type"
)
@BsonDiscriminator
public abstract class Event implements Comparable<Event> {

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

    @Override
    public int compareTo(Event e) {
        return createdAt.compareTo(e.createdAt);
    }
    
}
