package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.events.request.*;

import java.util.Collections;
import java.util.List;

public class AggregateBuilder {

    public static PartnerRequest buildAggregate(List<PartnerRequestEvent> events){
        PartnerRequest partnerRequest = new PartnerRequest();
        Collections.sort(events);
        for (PartnerRequestEvent event : events) {

            event.accept(new PartnerRequestVisitor() {

                @Override
                public void visit(RequestAcceptedEvent event) {
                    partnerRequest.apply(event);
                }

                @Override
                public void visit(RequestInitiatedEvent event) {
                    partnerRequest.apply(event);
                }

                @Override
                public void visit(RequestUpdatedEvent event) {
                    partnerRequest.apply(event);
                }

                @Override
                public void visit(RequestCancelledEvent event) {
                    partnerRequest.apply(event);
                }

                @Override
                public void visit(RequestOpenedEvent event) {
                    partnerRequest.apply(event);
                }

                @Override
                public void visit(RequestAcceptPendingEvent event) {
                    partnerRequest.apply(event);
                }

                @Override
                public void visit(RequestRevertPendingEvent event) {
                    partnerRequest.apply(event);
                }
            });
        }
        return partnerRequest;
    }
}
