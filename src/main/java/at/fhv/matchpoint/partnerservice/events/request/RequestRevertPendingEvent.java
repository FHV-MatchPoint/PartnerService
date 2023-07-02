package at.fhv.matchpoint.partnerservice.events.request;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.AggregateType;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateFailedEvent;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestRevertPendingEvent extends PartnerRequestEvent {

    public String ownerId;
    public String tennisClubId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public RequestState state;

    public RequestRevertPendingEvent() {
    }

    private RequestRevertPendingEvent(AggregateType aggregateType, String aggregateId) {
        super(aggregateType, aggregateId);
        this.state = RequestState.OPEN;
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }

    public static RequestRevertPendingEvent create(SessionCreateFailedEvent sessionCreateFailedEvent,
            PartnerRequest partnerRequest) {
        return new RequestRevertPendingEvent(
                AggregateType.PARTNERREQUEST,
                UUID.randomUUID().toString());
    }
}
