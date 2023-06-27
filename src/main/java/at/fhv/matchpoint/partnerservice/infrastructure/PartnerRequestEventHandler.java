package at.fhv.matchpoint.partnerservice.infrastructure;

import java.util.Optional;

import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import at.fhv.matchpoint.partnerservice.events.court.*;
import at.fhv.matchpoint.partnerservice.events.request.*;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.PartnerRequestReadModelRepository;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestVisitor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PartnerRequestEventHandler {

    @Inject
    PartnerRequestReadModelRepository partnerRequestReadModelRepository;

    @Transactional
    public void handleEvent(PartnerRequestEvent event) {
        Optional<PartnerRequestReadModel> optPartnerRequest = partnerRequestReadModelRepository.findByIdOptional(event.aggregateId);
        PartnerRequestReadModel partnerRequestReadModel = new PartnerRequestReadModel();
        if (optPartnerRequest.isPresent()) {
            partnerRequestReadModel = optPartnerRequest.get();
        }
        partnerRequestReadModelRepository.persistAndFlush(applyEvent(partnerRequestReadModel, event));
    }

    @Transactional
    public void handleEvent(CourtEvent event) {
        Optional<PartnerRequestReadModel> optPartnerRequest = partnerRequestReadModelRepository.findByIdOptional(event.aggregateId);
        PartnerRequestReadModel partnerRequestReadModel = new PartnerRequestReadModel();
        if (optPartnerRequest.isPresent()) {
            partnerRequestReadModel = optPartnerRequest.get();
        }
        partnerRequestReadModelRepository.persistAndFlush(applyEvent(partnerRequestReadModel, event));
    }

    private PartnerRequestReadModel applyEvent(PartnerRequestReadModel model, PartnerRequestEvent event) {
        PartnerRequestReadModel requestReadModel = model;
        event.accept(new PartnerRequestVisitor() {

            @Override
            public void visit(RequestAcceptedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestInitiatedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestUpdatedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestCancelledEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestOpenedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestAcceptPendingEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestRevertPendingEvent event) {
                requestReadModel.apply(event);
            }
        });
        return requestReadModel;
    }

    private PartnerRequestReadModel applyEvent(PartnerRequestReadModel model, CourtEvent event) {
        PartnerRequestReadModel requestReadModel = model;
        event.accept(new PartnerRequestCourtVisitor() {
            @Override
            public void visit(RequestInitiateFailedEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(RequestInitiateSucceededEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(SessionCreateSucceededEvent event) {
                requestReadModel.apply(event);
            }

            @Override
            public void visit(SessionCreateFailedEvent event) {
                requestReadModel.apply(event);
            }
        });
        return requestReadModel;
    }

}
