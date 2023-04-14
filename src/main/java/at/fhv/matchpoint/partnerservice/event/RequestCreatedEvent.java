package at.fhv.matchpoint.partnerservice.event;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestCreatedEvent extends Event {

    public String ownerId;

    public RequestCreatedEvent(){
        this.ownerId = "Markomannen";
    }
    
}
