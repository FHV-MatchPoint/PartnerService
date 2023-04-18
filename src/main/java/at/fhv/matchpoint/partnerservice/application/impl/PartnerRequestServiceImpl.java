package at.fhv.matchpoint.partnerservice.application.impl;

import at.fhv.matchpoint.partnerservice.application.PartnerRequestService;
import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequest;
import at.fhv.matchpoint.partnerservice.event.Event;
import at.fhv.matchpoint.partnerservice.event.EventRepository;
import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class PartnerRequestServiceImpl implements PartnerRequestService {

    @Inject
    EventRepository eventRepository;

    @Override
    public PartnerRequest createPartnerRequest(@Valid CreatePartnerRequestCommand createPartnerRequestCommand) {
//        this.verifyCreateRequest();
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestCreatedEvent event = partnerRequest.process(createPartnerRequestCommand);
        try {
            eventRepository.persist(event);
        } catch (Exception exception) {
//            throw error
        }
        return partnerRequest;
    }

    @Override
    public PartnerRequest acceptPartnerRequest(@Valid AcceptPartnerRequestCommand acceptPartnerRequestCommand) {
//        create AcceptPartnerRequestCommand
        PartnerRequest partnerRequest = new PartnerRequest();
//        for (Event event: eventRepository.findAll().list()
//             ) {
//            partnerRequest.apply(event);
//        }
//        partnerRequest.process(command);
        return null;
    }

    @Override
    public PartnerRequest updatePartnerRequest(@Valid UpdatePartnerRequestCommand updatePartnerRequestCommand) {
        return null;
    }

    @Override
    public PartnerRequest cancelPartnerRequest(@Valid CancelPartnerRequestCommand cancelPartnerRequestCommand) {
        return null;
    }

    @Override
    public List<Event> getEvents(String memberId, String clubId, LocalDate from, LocalDate to) {
        return null;
    }
}
