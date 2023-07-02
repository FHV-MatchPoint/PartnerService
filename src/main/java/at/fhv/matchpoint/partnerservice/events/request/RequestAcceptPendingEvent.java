package at.fhv.matchpoint.partnerservice.events.request;

import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.AggregateType;
import at.fhv.matchpoint.partnerservice.utils.CustomDateTimeFormatter;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.time.LocalDate;
import java.time.LocalTime;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestAcceptPendingEvent extends PartnerRequestEvent {

    public String ownerId;
    public String tennisClubId;
    public String partnerId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public RequestState state;

    public RequestAcceptPendingEvent(){}

    private RequestAcceptPendingEvent(AggregateType aggregateType, String aggregateId,
                                      String ownerId, String tennisClubId, String partnerId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(aggregateType, aggregateId);
        this.ownerId = ownerId;
        this.tennisClubId = tennisClubId;
        this.partnerId = partnerId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = RequestState.ACCEPTED;
    }

    public static RequestAcceptPendingEvent create(AcceptPartnerRequestCommand command, PartnerRequest partnerRequest) throws DateTimeFormatException {
        return new RequestAcceptPendingEvent(
                AggregateType.PARTNERREQUEST,
                command.getPartnerRequestId(),
                partnerRequest.getOwnerId(),
                partnerRequest.getClubId(),
                command.getPartnerId(),
                partnerRequest.getDate(),
                CustomDateTimeFormatter.parseTime(command.getStartTime()),
                CustomDateTimeFormatter.parseTime(command.getEndTime()));
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }
}
