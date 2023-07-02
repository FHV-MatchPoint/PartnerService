package at.fhv.matchpoint.partnerservice.application.impl;

import at.fhv.matchpoint.partnerservice.application.LockPartnerRequestService;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.events.request.PartnerRequestEvent;
import at.fhv.matchpoint.partnerservice.events.request.RequestCancelledEvent;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.EventRepository;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.VersionNotMatchingException;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static at.fhv.matchpoint.partnerservice.utils.AggregateBuilder.buildAggregate;

@ApplicationScoped
public class LockPartnerRequestServiceImpl implements LockPartnerRequestService {

    @Inject
    EventRepository eventRepository;

    @Override
    public void lockPartnerRequestByMemberId(String memberId) throws Exception {
        Set<String> aggregateIds = eventRepository
                .list("ownerId = ?1 or partnerId = ?2", memberId, memberId)
                .stream().map(event -> event.aggregateId).collect(Collectors.toSet());
        cancelPartnerRequests(aggregateIds);
    }

    @Override
    public void lockPartnerRequestByClubId(String clubId) throws Exception {
        Set<String> aggregateIds = eventRepository
                .list("tennisClubId = ?1", clubId)
                .stream().map(event -> event.aggregateId).collect(Collectors.toSet());
        cancelPartnerRequests(aggregateIds);
    }

    private void cancelPartnerRequests(Set<String> aggregateIds) throws Exception {
        boolean allLocked = true;
        List<List<PartnerRequestEvent>> partnerRequestEvents = eventRepository
                .find("aggregateId in :ids", Parameters.with("ids", aggregateIds.toArray()))
                .stream().collect(Collectors.groupingBy(event -> event.aggregateId))
                .values().stream().collect(Collectors.toList());

        for (List<PartnerRequestEvent> events : partnerRequestEvents){
            PartnerRequest partnerRequest = buildAggregate(events);
            CancelPartnerRequestCommand command = new CancelPartnerRequestCommand();
            command.setMemberId(partnerRequest.getOwnerId());
            command.setPartnerRequestId(partnerRequest.getPartnerRequestId());
            try {
                RequestCancelledEvent event = partnerRequest.process(command);
                checkForVersionMismatch(events, partnerRequest);
                eventRepository.persist(event);
                partnerRequest.apply(event);
            } catch (PartnerRequestAlreadyCancelledException e) {
                // If already cancelled nothing happens
            } catch (Exception e) {
                allLocked = false;
            }
        }
        if (!allLocked) {
            throw new Exception();
        }
    }

    private List<PartnerRequestEvent> getEventsByAggregateId(String aggregateId) {
        return eventRepository.find("aggregateId", aggregateId).list();
    }

    private void checkForVersionMismatch(List<PartnerRequestEvent> events, PartnerRequest partnerRequest) throws VersionNotMatchingException {
        if(events.size() != getEventsByAggregateId(partnerRequest.getPartnerRequestId()).size()){
            throw new VersionNotMatchingException();
        }
    }
}
