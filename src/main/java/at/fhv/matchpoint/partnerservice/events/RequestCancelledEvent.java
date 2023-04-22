package at.fhv.matchpoint.partnerservice.events;

import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@MongoEntity(collection = "Event")
@BsonDiscriminator
public class RequestCancelledEvent extends Event {

    public RequestCancelledEvent() {}

    private RequestCancelledEvent(AggregateType aggregateType, String aggregateId) {
        super(aggregateType, aggregateId);
    }

    public static RequestCancelledEvent create(CancelPartnerRequestCommand command){
        return new RequestCancelledEvent(
                AggregateType.PARTNERREQUEST,
                command.getPartnerRequestId()
        );
    }

    @Override
    public void accept(PartnerRequestVisitor v) {
        v.visit(this);
    }

}
