package at.fhv.matchpoint.partnerservice.infrastructure.remote;

import java.util.Optional;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.domain.model.TimeSlot;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@IfBuildProfile("prod")
public class RemoteServicesImpl implements RemoteServices {

    @Override
    public Optional<Member> verify(String memberId) {
        return null;
    }

    @Override
    public Optional<TimeSlot> getAvailableTimeframe(String memberId, String date, String startTime, String endTime) {
        return null;
    }
}
