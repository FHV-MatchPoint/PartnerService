package at.fhv.matchpoint.partnerservice.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequestVisitor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestCreatedEvent extends Event {

    public String ownerId;
    public String tennisClubId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    public RequestCreatedEvent(){}

    private RequestCreatedEvent(AggregateType aggregateType, String aggregateId, String ownerId, String tennisClubId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(aggregateType, aggregateId);
        this.ownerId = ownerId;
        this.tennisClubId = tennisClubId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static RequestCreatedEvent create(CreatePartnerRequestCommand command) {
        return new RequestCreatedEvent(AggregateType.PARTNERREQUEST, UUID.randomUUID().toString(), command.getMemberId(), command.getClubId(), command.getDate(), command.getStartTime(), command.getEndTime());
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }
}
