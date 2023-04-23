package at.fhv.matchpoint.partnerservice.application.impl;

import at.fhv.matchpoint.partnerservice.application.PartnerRequestService;
import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.*;

import at.fhv.matchpoint.partnerservice.infrastructure.EventRepository;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.RequestStateChangeException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.VersionNotMatchingException;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static at.fhv.matchpoint.partnerservice.utils.AggregateBuilder.buildAggregate;

@ApplicationScoped
public class PartnerRequestServiceImpl implements PartnerRequestService {

    @Inject
    EventRepository eventRepository;

    @Override
    public PartnerRequestDTO initiatePartnerRequest(InitiatePartnerRequestCommand initiatePartnerRequestCommand) throws DateTimeFormatException, MongoDBPersistenceError {
//        this.verifyCreateRequest();
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestInitiatedEvent event = partnerRequest.process(initiatePartnerRequestCommand);
        try {
            eventRepository.persist(event);
            partnerRequest.apply(event);
        } catch (Exception exception) {
            throw new MongoDBPersistenceError();
        }
        return PartnerRequestDTO.buildDTO(partnerRequest);
    }

    @Override
    public PartnerRequestDTO acceptPartnerRequest(AcceptPartnerRequestCommand acceptPartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException {
//        create AcceptPartnerRequestCommand
        List<Event> events = getEventsByAggregateId(acceptPartnerRequestCommand.getPartnerRequestId());
        if(events.size() == 0){
            throw new PartnerRequestNotFoundException();
        }
        PartnerRequest partnerRequest = buildAggregate(events);
        RequestAcceptedEvent event = partnerRequest.process(acceptPartnerRequestCommand);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        checkForVersionMismatch(events, partnerRequest);
        try {
            eventRepository.persist(event);
            partnerRequest.apply(event);
        } catch (Exception exception) {
            throw new MongoDBPersistenceError();
        }
        return PartnerRequestDTO.buildDTO(partnerRequest);
    }

    @Override
    public PartnerRequestDTO updatePartnerRequest(UpdatePartnerRequestCommand updatePartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException {
        List<Event> events = getEventsByAggregateId(updatePartnerRequestCommand.getPartnerRequestId());
        if(events.size() == 0){
            throw new PartnerRequestNotFoundException();
        }
        PartnerRequest partnerRequest = buildAggregate(events);
        RequestUpdatedEvent event = partnerRequest.process(updatePartnerRequestCommand);
        checkForVersionMismatch(events, partnerRequest);
        try {
            eventRepository.persist(event);
            partnerRequest.apply(event);
        } catch (Exception exception) {
            throw new MongoDBPersistenceError();
        }
        return PartnerRequestDTO.buildDTO(partnerRequest);
    }

    @Override
    public PartnerRequestDTO cancelPartnerRequest(CancelPartnerRequestCommand cancelPartnerRequestCommand) throws MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException {
        List<Event> events = getEventsByAggregateId(cancelPartnerRequestCommand.getPartnerRequestId());
        if(events.size() == 0){
            throw new PartnerRequestNotFoundException();
        }
        PartnerRequest partnerRequest = buildAggregate(events);
        RequestCancelledEvent event = partnerRequest.process(cancelPartnerRequestCommand);
        checkForVersionMismatch(events, partnerRequest);
        try {
            eventRepository.persist(event);
            partnerRequest.apply(event);
        } catch (Exception exception) {
            throw new MongoDBPersistenceError();
        }
        return PartnerRequestDTO.buildDTO(partnerRequest);
    }

    @Override
    public PartnerRequestDTO getPartnerRequestById(String memberId, String partnerRequestId) throws PartnerRequestNotFoundException {
        // verify member
        List<Event> events = getEventsByAggregateId(partnerRequestId);
        if(events.size() == 0){
            throw new PartnerRequestNotFoundException();
        }
        return PartnerRequestDTO.buildDTO(buildAggregate(events));
    }

    @Override
    public List<PartnerRequestDTO> getOpenPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to) {
        // verify member
        Set<String> aggregateIds = eventRepository
                .list("tennisClubId =?1", clubId)
                .stream().map(event -> event.aggregateId).collect(Collectors.toSet());
        return eventRepository.find("aggregateId in :ids", Parameters.with("ids", aggregateIds.toArray()))
                .stream().collect(Collectors.groupingBy(event -> event.aggregateId))
                .values().stream().map(eventList -> buildAggregate(eventList))
                .filter(partnerRequest -> ( partnerRequest.getState().equals(RequestState.INITIATED) &&
                        (partnerRequest.getDate().isBefore(to) && partnerRequest.getDate().isAfter(from))
                        && !partnerRequest.getOwnerId().equals(memberId)))
                .map(partnerRequest -> PartnerRequestDTO.buildDTO(partnerRequest)).collect(Collectors.toList());
    }

    @Override
    public List<PartnerRequestDTO> getPartnerRequestsByMemberId(String memberId) {
        // verify member
        Set<String> aggregateIds = eventRepository.list("ownerId =?1 or partnerId = ?2", memberId, memberId)
                .stream().map(event -> event.aggregateId).collect(Collectors.toSet());
        return eventRepository.find("aggregateId in :ids", Parameters.with("ids", aggregateIds.toArray()))
                .stream().collect(Collectors.groupingBy(event -> event.aggregateId))
                .values().stream().map(eventList -> buildAggregate(eventList))
                .map(partnerRequest -> PartnerRequestDTO.buildDTO(partnerRequest)).collect(Collectors.toList());
    }

    private List<Event> getEventsByAggregateId(String aggregateId) {
        return eventRepository.find("aggregateId", aggregateId).list();
    }

    private void checkForVersionMismatch(List<Event> events, PartnerRequest partnerRequest) throws VersionNotMatchingException {
        if(events.size() != getEventsByAggregateId(partnerRequest.getPartnerRequestId()).size()){
            throw new VersionNotMatchingException();
        }
    }

}
