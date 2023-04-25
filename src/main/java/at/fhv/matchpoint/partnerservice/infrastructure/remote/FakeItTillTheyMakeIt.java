package at.fhv.matchpoint.partnerservice.infrastructure.remote;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@DefaultBean
public class FakeItTillTheyMakeIt implements RemoteServices {

    @Override
    public Member verify(String memberId) {
        return Member.create("TestMember", "TestClub", "Reiner Funden");
    }

    @Override
    public Response getAvailableTimeframe(String memberId, String date, String startTime, String endTime) {
        return Response.status(Response.Status.OK).entity("ok").build();
    }
}
