package at.fhv.matchpoint.partnerservice.rest;

import at.fhv.matchpoint.partnerservice.utils.exceptions.ResponseException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;

public class ResponseExceptionBuilder {

    public static Response buildMissingJSONFieldsResponse(ConstraintViolationException e){
        return Response.status(422).entity("Not all fields were set. Please ensure that all fields are set and not null").build();
    }

    public static Response buildDateTimeErrorResponse(ResponseException e) {
        return Response.status(e.getStatusCode()).entity(e.getResponseMessage()).build();
    }
}
