package at.fhv.matchpoint.partnerservice.rest;

import java.time.LocalDate;
import java.util.List;

import at.fhv.matchpoint.partnerservice.utils.CustomDateTimeFormatter;
import at.fhv.matchpoint.partnerservice.utils.ResponseExceptionBuilder;
import at.fhv.matchpoint.partnerservice.utils.exceptions.ResponseException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.application.impl.PartnerRequestServiceImpl;
import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.EventRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.repository.MemberRepository;

@Authenticated
@Path("partnerRequest")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "PartnerRequest-Endpoints")
@APIResponse(
    responseCode = "401", description = "Unauthorized")
@APIResponse(
    responseCode = "403", description = "Invalid MemberId")
@APIResponse(
    responseCode = "500", description = "Server Error")
public class PartnerRequestResource {

    @Inject
    PartnerRequestServiceImpl partnerRequestService;

    @Inject
    MemberRepository memberRepository;

    @Inject
    EventRepository eventRepository;

    @GET
    public List<Member> test(){
        return memberRepository.findAll().list();
    }

    @POST
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponse(
        responseCode = "422", description = "Wrong Date Format")
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest successfully created",
        responseCode = "201")
    @Operation(
        summary = "Create a PartnerRequest",
        description = "Create a PartnerRequest for the given date and time period")
    public Response create(InitiatePartnerRequestCommand initiatePartnerRequestCommand) {
        try {
            return Response.status(Status.CREATED).entity(partnerRequestService.initiatePartnerRequest(initiatePartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        } catch (ResponseException e) {
            return ResponseExceptionBuilder.buildDateTimeErrorResponse(e);
        }
    }

    @PUT
    @Path("accept")
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponse(
        responseCode = "422", description = "Wrong Date Format")
    @APIResponse(
        responseCode = "412", description = "Version Mismatch")
    @APIResponse(
        responseCode = "404", description = "Invalid PartnerRequestId")
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest successfully accepted",
        responseCode = "200")
    @Operation(
        summary = "Accept a PartnerRequest",
        description = "Accept a PartnerRequest and select the time period for the booking to be created")
    public Response accept(AcceptPartnerRequestCommand acceptPartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.acceptPartnerRequest(acceptPartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        } catch (ResponseException e) {
            return ResponseExceptionBuilder.buildDateTimeErrorResponse(e);
        }
    }

    @PUT
    @Path("update")
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponse(
        responseCode = "422", description = "Wrong Date Format")
    @APIResponse(
        responseCode = "412", description = "Version Mismatch")
    @APIResponse(
        responseCode = "404", description = "Invalid PartnerRequestId")
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest successfully updated",
        responseCode = "200")
    @Operation(
        summary = "Update time period of PartnerRequest",
        description = "Update the start end end time of the PartnerRequest with the given id")
    public Response update(UpdatePartnerRequestCommand updatePartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.updatePartnerRequest(updatePartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        } catch (ResponseException e) {
            return ResponseExceptionBuilder.buildDateTimeErrorResponse(e);
        }
    }

    @PUT
    @Path("cancel")
    @APIResponse(
        responseCode = "400", description = "Missing JSON Fields")
    @APIResponse(
        responseCode = "412", description = "Version Mismatch")
    @APIResponse(
        responseCode = "404", description = "Invalid PartnerRequestId")
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest successfully canceled",
        responseCode = "200")
    @Operation(
        summary = "Cancel PartnerRequest",
        description = "Cancel PartnerRequest with the given id")
    public Response cancel(CancelPartnerRequestCommand cancelPartnerRequestCommand) {
        try {
            return Response.ok(partnerRequestService.cancelPartnerRequest(cancelPartnerRequestCommand)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        } catch (ResponseException e) {
            return ResponseExceptionBuilder.buildDateTimeErrorResponse(e);
        }
    }

    @GET
    @Path("openRequests/member/{memberId}/")
    @APIResponse(
        responseCode = "422", description = "Wrong Date Format")
    @APIResponse(
        responseCode = "404", description = "Invalid ClubId")
    @APIResponse(
        responseCode = "400", description = "Missing Query Parameters")
    @APIResponse(content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = PartnerRequestDTO.class)),
        description = "PartnerRequests found", responseCode = "200")
    @Operation(
        summary = "Find open PartnerRequests",
        description = "Find available PartnerRequests in the given time frame")
    public Response getOpenPartnerRequests(@PathParam("memberId") String memberId, @QueryParam("tennisClubId") String tennisClubId, @QueryParam("from") String from, @QueryParam("to") String to) {
        try {
            LocalDate fromDate = CustomDateTimeFormatter.parseDate(from);
            LocalDate toDate = CustomDateTimeFormatter.parseDate(to);
            return Response.ok(partnerRequestService.getOpenPartnerRequests(memberId, tennisClubId, fromDate, toDate)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        } catch (ResponseException e) {
            return ResponseExceptionBuilder.buildDateTimeErrorResponse(e);
        }
    }

    @GET
    @Path("{partnerRequestId}/member/{memberId}")
    @APIResponse(
        responseCode = "404", description = "Invalid PartnerRequestId")
    @APIResponse(
        responseCode = "400", description = "Missing Path Parameters")
    @APIResponseSchema(value = PartnerRequestDTO.class,
        responseDescription = "PartnerRequest found",
        responseCode = "200")
    @Operation(
        summary = "Find PartnerRequest by PartnerRequestId",
        description = "Find PartnerRequests with the given PartnerRequestId")
    public Response getByPartnerRequestId(@PathParam("memberId") String memberId, @PathParam("partnerRequestId") String partnerRequestId) {
        try {
            return Response.ok(partnerRequestService.getPartnerRequestById(memberId, partnerRequestId)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        } catch (ResponseException e) {
            return ResponseExceptionBuilder.buildDateTimeErrorResponse(e);
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
        description = "Find all PartnerRequests where the given Member is either Owner or Partner")
    public Response getPartnerRequestByMemberId(@PathParam("memberId") String memberId) {
        try {
            return Response.ok(partnerRequestService.getPartnerRequestsByMemberId(memberId)).build();
        } catch (ConstraintViolationException e) {
            return ResponseExceptionBuilder.buildMissingJSONFieldsResponse(e);
        } catch (ResponseException e) {
            return ResponseExceptionBuilder.buildDateTimeErrorResponse(e);
        }
    }

    @PUT
    @Path("/cancel/member/{memberId}")
    @Produces(MediaType.TEXT_PLAIN)
    @Tag(name="XForbidden")
    @APIResponse(
        responseCode = "200", description = "Bad Communication Decision")
    @Operation(
        summary = "Cancel all PartnerRequest of a Member",
        description = "Cancel all open PartnerRequest of a Member as a result of a Member Lock event")
    public String lockMemberHandler(@PathParam("memberId") String memberId){
        return "This should be handled with asynchronous messaging. Therefore this endpoint will only return this string. USE REDIS STREAMS!\n\nWe recommend Kafka though";
    }

    @PUT
    @Path("/cancel/club/{clubId}")
    @Produces(MediaType.TEXT_PLAIN)
    @Tag(name="XForbidden")
    @APIResponse(
        responseCode = "200", description = "Bad Communication Decision")
    @Operation(
        summary = "Cancel all PartnerRequest at one Tennis Club",
        description = "Cancel all open PartnerRequest at one Tennis Club as a result of a Tennis Club Lock event")
    public String lockTennisClubHandler(@PathParam("clubId") String clubId){
        return "This should be handled with asynchronous messaging. Therefore this endpoint will only return this string. USE REDIS STREAMS!\n\nWe recommend Kafka though";
    }
}