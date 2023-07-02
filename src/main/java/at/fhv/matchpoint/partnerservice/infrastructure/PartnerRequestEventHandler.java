package at.fhv.matchpoint.partnerservice.infrastructure;

import java.util.Optional;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import at.fhv.matchpoint.partnerservice.events.PartnerRequestEventTracking;
import at.fhv.matchpoint.partnerservice.events.request.*;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.PartnerRequestEventTrackingRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.PartnerRequestReadModelRepository;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MessageAlreadyProcessedException;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PartnerRequestEventHandler {

    @Inject
    PartnerRequestReadModelRepository partnerRequestReadModelRepository;

    @Inject
    PartnerRequestEventTrackingRepository partnerRequestEventTrackingRepository;

    @Transactional
    public void handleEvent(PartnerRequestEvent event) throws MessageAlreadyProcessedException {
        PartnerRequestEventTracking lastProcessedEvent;
        PartnerRequestEventTracking newTrackedEvent = new PartnerRequestEventTracking(event);
        try {
            lastProcessedEvent = partnerRequestEventTrackingRepository.find("aggregateId", Sort.by("createdAt").descending(), event.aggregateId).firstResult();
            partnerRequestEventTrackingRepository.persist(newTrackedEvent);
        } catch (Exception e) {
            throw new MessageAlreadyProcessedException();
        }
        Optional<PartnerRequestReadModel> optPartnerRequest = partnerRequestReadModelRepository.findByIdOptional(event.aggregateId);
        PartnerRequestReadModel partnerRequestReadModel = new PartnerRequestReadModel();
        if (optPartnerRequest.isPresent()) {
            partnerRequestReadModel = optPartnerRequest.get();
        }
        partnerRequestReadModelRepository.persistAndFlush(applyEvent(partnerRequestReadModel, event, lastProcessedEvent));
    }

    private PartnerRequestReadModel applyEvent(PartnerRequestReadModel model, PartnerRequestEvent event, PartnerRequestEventTracking lastProcessedEvent) {
        PartnerRequestReadModel requestReadModel = model;
        event.accept(new PartnerRequestVisitor() {

            @Override
            public void visit(RequestAcceptedEvent event) {
                if (lastProcessedEvent != null && lastProcessedEvent.createdAt.isBefore(event.createdAt)) {
                    requestReadModel.apply(event);
                }
            }

            @Override
            public void visit(RequestInitiatedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestUpdatedEvent event) {
                if (lastProcessedEvent != null && lastProcessedEvent.createdAt.isBefore(event.createdAt)) {
                    requestReadModel.apply(event);
                }
            }

            @Override
            public void visit(RequestCancelledEvent event) {
                if (lastProcessedEvent != null && lastProcessedEvent.createdAt.isBefore(event.createdAt)) {
                    requestReadModel.apply(event);
                }
            }

            @Override
            public void visit(RequestOpenedEvent event) {
                if (lastProcessedEvent != null && lastProcessedEvent.createdAt.isBefore(event.createdAt)) {
                    requestReadModel.apply(event);
                }
            }

            @Override
            public void visit(RequestAcceptPendingEvent event) {
                if (lastProcessedEvent != null && lastProcessedEvent.createdAt.isBefore(event.createdAt)) {
                    requestReadModel.apply(event);
                }
            }

            @Override
            public void visit(RequestRevertPendingEvent event) {
                if (lastProcessedEvent != null && lastProcessedEvent.createdAt.isBefore(event.createdAt)) {
                    requestReadModel.apply(event);
                }
            }
        });
        return requestReadModel;
    }

}
