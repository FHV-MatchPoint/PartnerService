package at.fhv.matchpoint.partnerservice.rest;

import at.fhv.matchpoint.partnerservice.application.CacheService;
import at.fhv.matchpoint.partnerservice.application.impl.CacheServiceImpl;
import at.fhv.matchpoint.partnerservice.infrastructure.EventRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.LockClubListener;
import at.fhv.matchpoint.partnerservice.infrastructure.LockMemberListener;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("supersecret/{password}")
public class DevPoint {

    @Inject
    LockMemberListener lockMemberListener;

    @Inject
    LockClubListener lockClubListener;

    @Inject
    EventRepository eventRepository;

    @Inject
    CacheServiceImpl cacheService;

    @GET
    @Path("lockmember/{memberId}")
    public Response lockMember(@PathParam("password") String password, @PathParam("memberId") String memberId) {
        if (!password.equals("admin")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("So nicht mein Freund").build();
        }
        lockMemberListener.sendMessage(memberId);
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

    @GET
    @Path("cache")
    public Response cacheTest(@PathParam("password") String password) {
        if (!password.equals("admin")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("So nicht mein Freund").build();
        }
        long executionStart = System.currentTimeMillis();
        String res = this.cacheService.getCache();
        long executionEnd = System.currentTimeMillis();
        long duration = executionEnd - executionStart;
        String response = res + " " + duration;
        return Response.ok(response).build();
    }
}
