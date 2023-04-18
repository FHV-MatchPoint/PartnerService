package at.fhv.matchpoint.partnerservice.event;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestAcceptedEvent extends Event {

    public String partnerId;
    public LocalTime startTime;
    public LocalTime endTime;

    public RequestAcceptedEvent(){
        super(LocalDateTime.now(), "PartnerRequest", "1");
        this.partnerId = "Helvetier";
        this.startTime = LocalTime.now();
        this.endTime = LocalTime.MIDNIGHT;
    }

}
