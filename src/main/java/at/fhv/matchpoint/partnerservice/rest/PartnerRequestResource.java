package at.fhv.matchpoint.partnerservice.rest;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import at.fhv.matchpoint.partnerservice.application.impl.PartnerRequestServiceImpl;
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

@Path("partnerRequest")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "PartnerRequest-Endpoints")
public class PartnerRequestResource {

    // This has to be removed later as it is currently only used for development purposes
    @Inject
    EventRepository eventRepository;

    @Inject
    PartnerRequestServiceImpl partnerRequestService;

    @POST
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponseSchema(value = PartnerRequest.class,
        responseDescription = "PartnerRequest sucessfully created",
        responseCode = "200")
    @Operation(
        summary = "Create a PartnerRequest",
        description = "Create a ParnterRequest for the given date and time period")
    public Response create(CreatePartnerRequestCommand createPartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.createPartnerRequest(createPartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @PUT
    @Path("accept")
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponseSchema(value = PartnerRequest.class,
        responseDescription = "PartnerRequest sucessfully accepted",
        responseCode = "200")
    @Operation(
        summary = "Accept a PartnerRequest",
        description = "Accept a ParnterRequest and selecet the time period for the booking to be created")
    public Response accept(AcceptPartnerRequestCommand acceptPartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.acceptPartnerRequest(acceptPartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @PUT
    @Path("update")
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponseSchema(value = PartnerRequest.class,
        responseDescription = "PartnerRequest sucessfully updated",
        responseCode = "200")
    @Operation(
        summary = "Update time period of PartnerRequest",
        description = "Update the start end end time of the PartnerRequest with the given id")
    public Response update(UpdatePartnerRequestCommand updatePartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.updatePartnerRequest(updatePartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @PUT
    @Path("cancel")
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponseSchema(value = PartnerRequest.class,
        responseDescription = "PartnerRequest sucessfully canceled",
        responseCode = "200")
    @Operation(
        summary = "Cancel PartnerRequest",
        description = "Cancel PartnerRequest with the given id")
    public Response cancle(CancelPartnerRequestCommand cancelPartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.cancelPartnerRequest(cancelPartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @GET
    public Response find(@QueryParam("memberId") String memberId, @QueryParam("tennisClubId") String tennisClubId, @QueryParam("from") LocalDate from, @QueryParam("to") LocalDate to) {
        try {
            return Response.ok(partnerRequestService.getEvents(memberId, tennisClubId, from, to)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }



    /*
     * The following are only temporary devopment restpoints
     */

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

    @GET
    @Path("events")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> events(){
        return eventRepository.listAll();
    }



}