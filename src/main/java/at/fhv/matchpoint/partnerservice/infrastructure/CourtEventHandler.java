package at.fhv.matchpoint.partnerservice.infrastructure;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.events.court.CourtEvent;
import at.fhv.matchpoint.partnerservice.events.court.RedisCourtEvent;
import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.RequestInitiateSucceededEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateFailedEvent;
import at.fhv.matchpoint.partnerservice.events.court.SessionCreateSucceededEvent;
import at.fhv.matchpoint.partnerservice.events.request.PartnerRequestEvent;
import at.fhv.matchpoint.partnerservice.events.request.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.events.request.RequestCancelledEvent;
import at.fhv.matchpoint.partnerservice.events.request.RequestOpenedEvent;
import at.fhv.matchpoint.partnerservice.events.request.RequestRevertPendingEvent;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.EventRepository;
import at.fhv.matchpoint.partnerservice.utils.AggregateBuilder;
import at.fhv.matchpoint.partnerservice.utils.PartnerRequestCourtVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestAlreadyCancelledException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.VersionNotMatchingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CourtEventHandler {


    @Inject
    EventRepository eventRepository;

    @Transactional
    public void handleEvent(RedisCourtEvent redisCourtEvent) throws PartnerRequestNotFoundException, VersionNotMatchingException, MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException, JsonMappingException, JsonProcessingException {
        
        CourtEvent courtEvent = CourtEvent.createFrom(redisCourtEvent);
        
        if(courtEvent != null) {

            List<PartnerRequestEvent> events = getEventsByAggregateId(courtEvent.getPartnerRequestId());
            if(events.size() == 0){
                throw new PartnerRequestNotFoundException();
            }
            PartnerRequest partnerRequest = AggregateBuilder.buildAggregate(events);
            courtEvent.accept(new PartnerRequestCourtVisitor() {

                @Override
                public void visit(RequestInitiateFailedEvent event) throws MongoDBPersistenceError, PartnerRequestAlreadyCancelledException {
                    RequestCancelledEvent cancelledEvent = partnerRequest.process(event);
                    try {
                        eventRepository.persist(cancelledEvent);
                        partnerRequest.apply(cancelledEvent);
                    } catch (Exception exception) {
                        throw new MongoDBPersistenceError();
                    }
                }

                @Override
                public void visit(RequestInitiateSucceededEvent event) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException {
                    RequestOpenedEvent openedEvent = partnerRequest.process(event);
                    try {
                        eventRepository.persist(openedEvent);
                        partnerRequest.apply(openedEvent);
                    } catch (Exception exception) {
                        throw new MongoDBPersistenceError();
                    }
                }

                @Override
                public void visit(SessionCreateSucceededEvent event) throws MongoDBPersistenceError, DateTimeFormatException, PartnerRequestAlreadyCancelledException {
                    RequestAcceptedEvent acceptedEvent = partnerRequest.process(event);
                    try {
                        eventRepository.persist(acceptedEvent);
                        partnerRequest.apply(acceptedEvent);
                    } catch (Exception exception) {
                        throw new MongoDBPersistenceError();
                    }
                }

                @Override
                public void visit(SessionCreateFailedEvent event) throws MongoDBPersistenceError, PartnerRequestAlreadyCancelledException {
                    RequestRevertPendingEvent requestRevertPendingEvent = partnerRequest.process(event);
                    try {
                        eventRepository.persist(requestRevertPendingEvent);
                        partnerRequest.apply(requestRevertPendingEvent);
                    } catch (Exception exception) {
                        throw new MongoDBPersistenceError();
                    }
                }
                
            });

        }
    }

    private List<PartnerRequestEvent> getEventsByAggregateId(String aggregateId) {
        return eventRepository.find("aggregateId", aggregateId).list();
    }
    
}
