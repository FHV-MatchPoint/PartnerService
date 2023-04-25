package at.fhv.matchpoint.partnerservice.infrastructure.remote;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@IfBuildProfile("prod")
public class RemoteServicesImpl implements RemoteServices {

    @Override
    public Member verify(String memberId) {
        return null;
    }

    @Override
    public Response getAvailableTimeframe(String memberId, String date, String startTime, String endTime) {
        return null;
    }
}
