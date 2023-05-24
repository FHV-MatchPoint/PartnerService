package at.fhv.matchpoint.partnerservice.rest;

import at.fhv.matchpoint.partnerservice.infrastructure.consumer.LockClubConsumer;
import at.fhv.matchpoint.partnerservice.infrastructure.consumer.MemberEventConsumer;
import at.fhv.matchpoint.partnerservice.infrastructure.reposistory.EventRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("supersecret/{password}")
@RolesAllowed("Admin")
public class DevPoint {

    @Inject
    MemberEventConsumer memberEventConsumer;

    @Inject
    LockClubConsumer lockClubListener;

    @Inject
    EventRepository eventRepository;

    @GET
    @Path("lockmember/{memberId}")
    public Response lockMember(@PathParam("password") String password, @PathParam("memberId") String memberId) {
        if (!password.equals("admin")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("So nicht mein Freund").build();
        }
        memberEventConsumer.sendMessage(memberId);
        return Response.ok().build();
    }

    @GET
    @Path("lockclub/{clubId}")
    public Response lockClub(@PathParam("password") String password, @PathParam("clubId") String clubId) {
        if (!password.equals("admin")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("So nicht mein Freund").build();
        }
        lockClubListener.sendMessage(clubId);
        return Response.ok().build();
    }

    @GET
    @Path("delete")
    public Response delete(@PathParam("password") String password) {
        if (!password.equals("admin")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("So nicht mein Freund").build();
        }
        return Response.ok().entity(eventRepository.deleteAll()).build();
    }

    @GET
    @Path("all")
    public Response all(@PathParam("password") String password) {
        if (!password.equals("admin")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("So nicht mein Freund").build();
        }
        return Response.ok(eventRepository.findAll().list()).build();
    }

}
