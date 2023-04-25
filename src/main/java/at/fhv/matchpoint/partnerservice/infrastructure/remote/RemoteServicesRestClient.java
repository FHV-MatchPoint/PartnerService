package at.fhv.matchpoint.partnerservice.infrastructure.remote;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "remote-service")
public interface RemoteServicesRestClient {

    @GET
    @Path("{memberId}")
    Member verify(@PathParam("memberId") String memberId);

    @GET
    @Path("{memberId}/{date}/{startTime}/{endTime}")
    Response getAvailableTimeframe(@PathParam("memberId") String memberId,
                                   @PathParam("date") String date,
                                   @PathParam("startTime") String startTime,
                                   @PathParam("endTime") String endTime);
}
