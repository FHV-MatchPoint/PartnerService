package at.fhv.matchpoint.partnerservice.application.impl;

import at.fhv.matchpoint.partnerservice.application.PartnerRequestService;
import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.event.Event;
import at.fhv.matchpoint.partnerservice.event.EventRepository;
import at.fhv.matchpoint.partnerservice.event.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PartnerRequestServiceImpl implements PartnerRequestService {

    @Inject
    EventRepository eventRepository;

    @Override
    public PartnerRequestDTO createPartnerRequest(CreatePartnerRequestCommand createPartnerRequestCommand) {
//        this.verifyCreateRequest();
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestCreatedEvent event = partnerRequest.process(createPartnerRequestCommand);
        try {
            eventRepository.persist(event);
            partnerRequest.apply(event);
        } catch (Exception exception) {
//            throw error
        }
        return PartnerRequestDTO.buildDTO(partnerRequest);
    }

    @Override
    public PartnerRequestDTO acceptPartnerRequest(AcceptPartnerRequestCommand acceptPartnerRequestCommand) {
//        create AcceptPartnerRequestCommand
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestAcceptedEvent event = partnerRequest.process(acceptPartnerRequestCommand);
//        for (Event event: eventRepository.findAll().list()
//             ) {
//            partnerRequest.apply(event);
//        }
//        partnerRequest.process(command);
        return null;
    }

    @Override
    public PartnerRequestDTO updatePartnerRequest(UpdatePartnerRequestCommand updatePartnerRequestCommand) {
        return null;
    }

    @Override
    public PartnerRequestDTO cancelPartnerRequest(CancelPartnerRequestCommand cancelPartnerRequestCommand) {
        return null;
    }

    @Override
    public PartnerRequestDTO getPartnerRequesById(String memberId, String partnerRequestId) {
        return PartnerRequestDTO.buildDTO(buildAggregate(eventRepository.find("aggregateId", partnerRequestId).list()));
    }

    @Override
    public List<PartnerRequestDTO> getPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public List<PartnerRequestDTO> getPartnerRequestsByMemberId(String memberId) {
        return null;
    }

    private PartnerRequest buildAggregate(List<Event> events){
        PartnerRequest partnerRequest = new PartnerRequest();
        for (Event event : events) {

            event.accept(new PartnerRequestVisitor() {

                @Override
                public void visit(RequestAcceptedEvent event) {
                    partnerRequest.apply(event);
                }

                @Override
                public void visit(RequestCreatedEvent event) {
                    partnerRequest.apply(event);
                }
            });
        }
        return partnerRequest;
    }

}
