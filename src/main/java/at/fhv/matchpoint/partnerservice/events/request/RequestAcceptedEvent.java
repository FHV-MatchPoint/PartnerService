package at.fhv.matchpoint.partnerservice.events.request;

import java.time.LocalDate;
import java.time.LocalTime;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.AggregateType;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateSucceededEvent;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.utils.CustomDateTimeFormatter;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestAcceptedEvent extends PartnerRequestEvent {

    public String ownerId;
    public String tennisClubId;
    public String partnerId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public RequestState state;

    public RequestAcceptedEvent(){}

    private RequestAcceptedEvent(AggregateType aggregateType, String aggregateId,
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

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }

    public static RequestAcceptedEvent create(SessionCreateSucceededEvent sessionCreateSucceededEvent,
            PartnerRequest partnerRequest) throws DateTimeFormatException {
        return new RequestAcceptedEvent(
                AggregateType.PARTNERREQUEST,
                sessionCreateSucceededEvent.getPartnerRequestId(),
                partnerRequest.getOwnerId(),
                partnerRequest.getClubId(),
                sessionCreateSucceededEvent.getPartnerId(),
                partnerRequest.getDate(),
                sessionCreateSucceededEvent.getStartTime(),
                sessionCreateSucceededEvent.getEndTime());
    }
}
