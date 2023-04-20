package at.fhv.matchpoint.partnerservice.rest;

import java.time.LocalDate;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.application.impl.PartnerRequestServiceImpl;
import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.event.EventRepository;

@Path("partnerRequest")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "PartnerRequest-Endpoints")
public class PartnerRequestResource {

    @Inject
    PartnerRequestServiceImpl partnerRequestService;

    @POST
    @Path("create")
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest successfully created",
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
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest sucessfully accepted",
        responseCode = "200")
    @Operation(
        summary = "Accept a PartnerRequest",
        description = "Accept a ParnterRequest and select the time period for the booking to be created")
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
    @APIResponseSchema(value = PartnerRequestDTO.class,
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
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest sucessfully canceled",
        responseCode = "200")
    @Operation(
        summary = "Cancel PartnerRequest",
        description = "Cancel PartnerRequest with the given id")
    public Response cancel(CancelPartnerRequestCommand cancelPartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.cancelPartnerRequest(cancelPartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @GET
    @APIResponse(
        responseCode = "400", description = "Missing Query Parameters")
    @APIResponse(content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = PartnerRequestDTO.class)),
        description = "PartnerRequests found", responseCode = "200")
    @Operation(
        summary = "Find PartnerRequests",
        description = "Find availbale PartnerRequests in the given time frame")
    public Response find(@QueryParam("memberId") String memberId, @QueryParam("tennisClubId") String tennisClubId, @QueryParam("from") LocalDate from, @QueryParam("to") LocalDate to) {
        try {
            return Response.ok(partnerRequestService.getPartnerRequests(memberId, tennisClubId, from, to)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @GET
    @Path("member/{memberId}/{partnerRequestId}")
    @APIResponse(
        responseCode = "400", description = "Missing Path Parameters")
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest found",
        responseCode = "200")
    @Operation(
        summary = "Find PartnerRequest by PartnerRequestId",
        description = "Find PartnerRequests with the given PartnerRequestId")
    public Response findByPartnerRequestId(@PathParam("memberId") String memberId, @PathParam("partnerRequestId") String partnerRequestId) {
        try {
            return Response.ok(partnerRequestService.getPartnerRequesById(memberId, partnerRequestId)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @GET
    @Path("member/{memberId}")
    @APIResponse(
        responseCode = "400", description = "Missing Path Parameters")
    @APIResponse(content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = PartnerRequestDTO.class)),
        description = "PartnerRequests found", responseCode = "200")
    @Operation(
        summary = "Find PartnerRequest by MemberId",
        description = "Find all PartnerRequests of the given Member")
    public Response findByMemberId(@PathParam("memberId") String memberId) {
        try {
            return Response.ok(partnerRequestService.getPartnerRequestsByMemberId(memberId)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        }
    }

    @PUT
    @Path("/member/{memberId}")
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponse(
        responseCode = "200", description = "Bad Communication Decision")
    @Operation(
        summary = "Cancel all PartnerRequest of a Member",
        description = "Cancel all open PartnerRequest of a Member as a result of a Member Lock event")
    public String lockMemberHandler(@PathParam("memberId") String memberId){
        return "This should be handled with asynchronous messaging. Therefore this endpoint will only return this string. USE REDIS STREAMS!\n\nWe recommend Kafka though";
    }

    @PUT
    @Path("/club/{clubId}")
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponse(
        responseCode = "200", description = "Bad Communication Decision")
    @Operation(
        summary = "Cancel all PartnerRequest at one Tennis Club",
        description = "Cancel all open PartnerRequest at one Tennis Club as a result of a Tennis Club Lock event")
    public String lockTennisClubHandler(@PathParam("clubId") String clubId){
        return "This should be handled with asynchronous messaging. Therefore this endpoint will only return this string. USE REDIS STREAMS!\n\nWe recommend Kafka though";
    }

    // @Inject
    // EventRepository eventRepository;

    // @GET
    // @Path("delete")
    // public Long delete(){
    //     return eventRepository.deleteAll();
    // }

    // @GET
    // @Path("all")
    // public Response all(){
    //     return Response.ok(eventRepository.findAll().list()).build();
    // }
}