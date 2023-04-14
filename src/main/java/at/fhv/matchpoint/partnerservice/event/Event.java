package at.fhv.matchpoint.partnerservice.event;

import java.time.LocalDateTime;

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
public abstract class Event {

    public ObjectId eventId;
    public LocalDateTime createdAt;
    public String aggregateType;
    public String aggregateId;
    
}
