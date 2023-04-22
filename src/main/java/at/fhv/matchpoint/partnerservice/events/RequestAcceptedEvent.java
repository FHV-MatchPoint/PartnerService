package at.fhv.matchpoint.partnerservice.events;

import java.time.LocalTime;

import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.utils.CustomDateTimeFormatter;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
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

    public static RequestAcceptedEvent create(AcceptPartnerRequestCommand command) throws DateTimeFormatException {
        return new RequestAcceptedEvent(
                AggregateType.PARTNERREQUEST,
                command.getPartnerRequestId(),
                command.getPartnerId(),
                CustomDateTimeFormatter.parseTime(command.getStartTime()),
                CustomDateTimeFormatter.parseTime(command.getEndTime()));
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }
}
