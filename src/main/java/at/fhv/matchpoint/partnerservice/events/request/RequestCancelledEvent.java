package at.fhv.matchpoint.partnerservice.events.request;

import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.AggregateType;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestCancelledEvent extends PartnerRequestEvent {

    public String ownerId;
    public String tennisClubId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public RequestState state;

    public RequestCancelledEvent() {}

    private RequestCancelledEvent(AggregateType aggregateType, String aggregateId,
                                  String ownerId, String tennisClubId, LocalDate date, LocalTime startTime, LocalTime endTime) {
       super(aggregateType, aggregateId);
       this.ownerId = ownerId;
       this.tennisClubId = tennisClubId;
       this.date = date;
       this.startTime = startTime;
       this.endTime = endTime;
       this.state = RequestState.CANCELLED;
   }

    public static RequestCancelledEvent create(CancelPartnerRequestCommand command, PartnerRequest partnerRequest){
        return new RequestCancelledEvent(
                AggregateType.PARTNERREQUEST,
                command.getPartnerRequestId(),
                partnerRequest.getOwnerId(),
                partnerRequest.getClubId(),
                partnerRequest.getDate(),
                partnerRequest.getStartTime(),
                partnerRequest.getEndTime()
        );
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }

}
