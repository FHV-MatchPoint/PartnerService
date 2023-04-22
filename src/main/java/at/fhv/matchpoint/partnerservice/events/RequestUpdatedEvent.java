package at.fhv.matchpoint.partnerservice.events;

import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.utils.CustomDateTimeFormatter;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.time.LocalDate;
import java.time.LocalTime;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestUpdatedEvent extends Event{

    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    public RequestUpdatedEvent(){}

    private RequestUpdatedEvent(AggregateType aggregateType, String aggregateId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(aggregateType, aggregateId);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static RequestUpdatedEvent create(UpdatePartnerRequestCommand command) throws DateTimeFormatException {
        return new RequestUpdatedEvent(
                AggregateType.PARTNERREQUEST,
                command.getPartnerRequestId(),
                CustomDateTimeFormatter.parseDate(command.getDate()),
                CustomDateTimeFormatter.parseTime(command.getStartTime()),
                CustomDateTimeFormatter.parseTime(command.getEndTime())
        );
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }
}
