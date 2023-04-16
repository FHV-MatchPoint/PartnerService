package at.fhv.matchpoint.partnerservice.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestCreatedEvent extends Event {

    public String partnerRequestId;
    public String ownerId;
    public String tennisClubId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;


    public RequestCreatedEvent(){
        super(LocalDateTime.now(), "PartnerRequest", "1");
        this.partnerRequestId = aggregateId;
        this.ownerId = "Markomannen";
        this.tennisClubId = "Panther";
        this.date = LocalDate.now();
        this.startTime = LocalTime.NOON;
        this.endTime = LocalTime.MIDNIGHT;
    }
    
}
