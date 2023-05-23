package at.fhv.matchpoint.partnerservice.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.utils.CustomDateTimeFormatter;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestInitiatedEvent extends Event {

    public String ownerId;
    public String tennisClubId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public RequestState state;

    public RequestInitiatedEvent() {
    }

    private RequestInitiatedEvent(AggregateType aggregateType, String aggregateId, String ownerId, String tennisClubId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(aggregateType, aggregateId);
        this.ownerId = ownerId;
        this.tennisClubId = tennisClubId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = RequestState.INITIATED;
    }

    public static RequestInitiatedEvent create(InitiatePartnerRequestCommand command) throws DateTimeFormatException {
        return new RequestInitiatedEvent(
                AggregateType.PARTNERREQUEST,
                UUID.randomUUID().toString(),
                command.getMemberId(),
                command.getClubId(),
                CustomDateTimeFormatter.parseDate(command.getDate()),
                CustomDateTimeFormatter.parseTime(command.getStartTime()),
                CustomDateTimeFormatter.parseTime(command.getEndTime()));
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }
}
