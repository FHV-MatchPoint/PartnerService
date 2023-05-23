package at.fhv.matchpoint.partnerservice.events;

import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
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

    public String ownerId;
    public String tennisClubId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public RequestState state;

    public RequestUpdatedEvent(){}

    private RequestUpdatedEvent(AggregateType aggregateType, String aggregateId,
        String ownerId, String tennisClubId, LocalDate date, LocalTime startTime, LocalTime endTime) {
       super(aggregateType, aggregateId);
       this.ownerId = ownerId;
       this.tennisClubId = tennisClubId;
       this.date = date;
       this.startTime = startTime;
       this.endTime = endTime;
       this.state = RequestState.INITIATED;
   }


    public static RequestUpdatedEvent create(UpdatePartnerRequestCommand command, PartnerRequest partnerRequest) throws DateTimeFormatException {
        return new RequestUpdatedEvent(
                AggregateType.PARTNERREQUEST,
                command.getPartnerRequestId(),
                partnerRequest.getOwnerId(),
                partnerRequest.getClubId(),
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
