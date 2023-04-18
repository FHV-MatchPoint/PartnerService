package at.fhv.matchpoint.partnerservice.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import at.fhv.matchpoint.partnerservice.application.impl.PartnerRequestServiceImpl;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequestVisitor;
import at.fhv.matchpoint.partnerservice.event.Event;
import at.fhv.matchpoint.partnerservice.event.EventRepository;
import at.fhv.matchpoint.partnerservice.event.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;

@Path("partnerRequest")
public class   PartnerRequestResource {

    @Inject
    EventRepository eventRepository;

    @Inject
    PartnerRequestServiceImpl partnerRequestService;

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(CreatePartnerRequestCommand createPartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.createPartnerRequest(createPartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("accept")
    @Produces(MediaType.TEXT_PLAIN)
    public Long accept() {
        Event event = new RequestAcceptedEvent();
        eventRepository.persist(event);
        return eventRepository.count();
    }

    @GET
    @Path("events")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> events(){
        return eventRepository.listAll();
    }

    @GET
    @Path("{partnerRequestId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PartnerRequest getPartnerRequestById(@PathParam(value = "partnerRequestId") String partnerRequestId){
        PartnerRequest partnerRequest = new PartnerRequest();
        for (Event event : eventRepository.find( "aggregateId", partnerRequestId).list()) {

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