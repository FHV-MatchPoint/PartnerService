package at.fhv.matchpoint.partnerservice.event;

import java.time.LocalTime;
import java.util.UUID;

import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequestVisitor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestAcceptedEvent extends Event {

    public String partnerId;
    public LocalTime startTime;
    public LocalTime endTime;

    public RequestAcceptedEvent(){}

    private RequestAcceptedEvent(AggregateType aggregateType, String aggregateId, String partnerId, LocalTime startTime, LocalTime endTime) {
        super(aggregateType, aggregateId);
        this.partnerId = partnerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static RequestAcceptedEvent create(AcceptPartnerRequestCommand command) {
        return new RequestAcceptedEvent(AggregateType.PARTNERREQUEST, UUID.randomUUID().toString(), command.getPartnerId(), command.getStartTime(), command.getEndTime());
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }
}
