package at.fhv.matchpoint.partnerservice.events.request;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.AggregateType;
import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateSucceededEvent;
import at.fhv.matchpoint.partnerservice.utils.CustomDateTimeFormatter;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.time.LocalDate;
import java.time.LocalTime;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestOpenedEvent extends PartnerRequestEvent {

    public String ownerId;
    public String tennisClubId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public RequestState state;

    public RequestOpenedEvent() {
    }

    private RequestOpenedEvent(AggregateType aggregateType, String aggregateId, String ownerId, String tennisClubId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(aggregateType, aggregateId);
        this.ownerId = ownerId;
        this.tennisClubId = tennisClubId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = RequestState.INITIATED;
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }

    public static RequestOpenedEvent create(RequestInitiateSucceededEvent requestInitiateSucceededEvent,
            PartnerRequest partnerRequest) throws DateTimeFormatException {
        return new RequestOpenedEvent(
                AggregateType.PARTNERREQUEST,
                partnerRequest.getPartnerRequestId(),
                partnerRequest.getOwnerId(),
                partnerRequest.getClubId(),
                requestInitiateSucceededEvent.getDate(),
                requestInitiateSucceededEvent.getStartTime(),
                requestInitiateSucceededEvent.getEndTime());
    }
}
