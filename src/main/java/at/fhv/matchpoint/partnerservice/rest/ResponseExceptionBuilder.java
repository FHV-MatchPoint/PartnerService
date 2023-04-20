package at.fhv.matchpoint.partnerservice.rest;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class ResponseExceptionBuilder {

    public static Response buildMissingJSONFieldsResponse(ConstraintViolationException e){
        return Response.status(Status.BAD_REQUEST).entity("Not all fields were set. Please ensure that all fields are set and not null").build();
    }
    
}