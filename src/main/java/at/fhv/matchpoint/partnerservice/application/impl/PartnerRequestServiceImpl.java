package at.fhv.matchpoint.partnerservice.application.impl;

import at.fhv.matchpoint.partnerservice.application.PartnerRequestService;
import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import at.fhv.matchpoint.partnerservice.events.*;

import at.fhv.matchpoint.partnerservice.infrastructure.EventRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.MemberRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.PartnerRequestReadModelRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.remote.RemoteServices;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotAuthorizedException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.RequestStateChangeException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.VersionNotMatchingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static at.fhv.matchpoint.partnerservice.utils.AggregateBuilder.buildAggregate;

@ApplicationScoped
public class PartnerRequestServiceImpl implements PartnerRequestService {

    @Inject
    EventRepository eventRepository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    RemoteServices remoteServices;

    @Inject
    PartnerRequestReadModelRepository partnerRequestReadModelRepository;

    @CacheInvalidate(cacheName = "openrequests-cache")
    @Override
    public PartnerRequestDTO initiatePartnerRequest(InitiatePartnerRequestCommand initiatePartnerRequestCommand) throws DateTimeFormatException, MongoDBPersistenceError, MemberNotAuthorizedException {
        Optional<Member> optMember = memberRepository.verify(initiatePartnerRequestCommand.getMemberId(), initiatePartnerRequestCommand.getClubId());
        if(!optMember.isPresent()){
            throw new MemberNotAuthorizedException("Not Authorized");
        }
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

    @CacheInvalidate(cacheName = "openrequests-cache")
    @Override
    public PartnerRequestDTO acceptPartnerRequest(AcceptPartnerRequestCommand acceptPartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException, MemberNotAuthorizedException {
        Optional<Member> optMember = memberRepository.verify(acceptPartnerRequestCommand.getPartnerId());
        if(!optMember.isPresent()){
            throw new MemberNotAuthorizedException( "Not Authorized");
        }
        List<Event> events = getEventsByAggregateId(acceptPartnerRequestCommand.getPartnerRequestId());
        if(events.size() == 0){
            throw new PartnerRequestNotFoundException();
        }
        PartnerRequest partnerRequest = buildAggregate(events);
        if(optMember.get().memberId.equals(partnerRequest.getOwnerId()) || !optMember.get().clubId.equals(partnerRequest.getClubId())){
            throw new MemberNotAuthorizedException("Cannot accept own Request");
        }
        RequestAcceptedEvent event = partnerRequest.process(acceptPartnerRequestCommand);
        checkForVersionMismatch(events, partnerRequest);
        try {
            eventRepository.persist(event);
            partnerRequest.apply(event);
        } catch (Exception exception) {
            throw new MongoDBPersistenceError();
        }
        return PartnerRequestDTO.buildDTO(partnerRequest);
    }

    @CacheInvalidate(cacheName = "openrequests-cache")
    @Override
    public PartnerRequestDTO updatePartnerRequest(UpdatePartnerRequestCommand updatePartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException, MemberNotAuthorizedException {
        Optional<Member> optMember = memberRepository.verify(updatePartnerRequestCommand.getMemberId());
        if(!optMember.isPresent()){
            throw new MemberNotAuthorizedException("Not Authorized");
        }
        List<Event> events = getEventsByAggregateId(updatePartnerRequestCommand.getPartnerRequestId());
        if(events.size() == 0){
            throw new PartnerRequestNotFoundException();
        }
        PartnerRequest partnerRequest = buildAggregate(events);
        if(!optMember.get().memberId.equals(partnerRequest.getOwnerId())){
            throw new MemberNotAuthorizedException("Cannot change foreign requests");
        }
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

    @CacheInvalidate(cacheName = "openrequests-cache")
    @Override
    public PartnerRequestDTO cancelPartnerRequest(CancelPartnerRequestCommand cancelPartnerRequestCommand) throws MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException, MemberNotAuthorizedException {
        Optional<Member> optMember = memberRepository.verify(cancelPartnerRequestCommand.getMemberId());
        if(!optMember.isPresent()){
            throw new MemberNotAuthorizedException("Not Authorized");
        }
        List<Event> events = getEventsByAggregateId(cancelPartnerRequestCommand.getPartnerRequestId());
        if(events.size() == 0){
            throw new PartnerRequestNotFoundException();
        }
        PartnerRequest partnerRequest = buildAggregate(events);
        if(!optMember.get().memberId.equals(partnerRequest.getOwnerId())){
            throw new MemberNotAuthorizedException("Not Authorized");
        }
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
    public PartnerRequestDTO getPartnerRequestById(String memberId, String partnerRequestId) throws PartnerRequestNotFoundException, MemberNotAuthorizedException {
        Optional<Member> optMember = memberRepository.verify(memberId);
        Optional<PartnerRequestReadModel> model = partnerRequestReadModelRepository.findByIdOptional(partnerRequestId);
        if(!optMember.isPresent() || !optMember.get().memberId.equals(memberId)){
            throw new MemberNotAuthorizedException("Not Authorized");
        }
        if(!model.isPresent()){
            throw new PartnerRequestNotFoundException();
        }
        return PartnerRequestDTO.buildDTO(model.get());
    }

    @CacheResult(cacheName = "openrequests-cache")
    @Override
    public List<PartnerRequestDTO> getOpenPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to) throws MemberNotAuthorizedException {
        Optional<Member> optMember = memberRepository.verify(memberId, clubId);
        if(!optMember.isPresent()){
            throw new MemberNotAuthorizedException("Not Authorized");
        }
        return partnerRequestReadModelRepository.getOpenPartnerRequests(memberId,clubId,from,to)
        .stream().map(PartnerRequestDTO::buildDTO).collect(Collectors.toList());
    }

    @Override
    public List<PartnerRequestDTO> getPartnerRequestsByMemberId(String memberId) throws MemberNotAuthorizedException {
        Optional<Member> optMember = memberRepository.verify(memberId);
        if(!optMember.isPresent()){
            throw new MemberNotAuthorizedException("Not Authorized");
        }
        return partnerRequestReadModelRepository.getPartnerRequestsByMemberId(memberId)
        .stream().map(PartnerRequestDTO::buildDTO).collect(Collectors.toList());
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
