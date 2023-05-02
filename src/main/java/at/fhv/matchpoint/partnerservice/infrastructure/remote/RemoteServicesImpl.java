package at.fhv.matchpoint.partnerservice.infrastructure.remote;

import java.util.Optional;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.domain.model.TimeSlot;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@IfBuildProfile("prod")
public class RemoteServicesImpl implements RemoteServices {

    @Inject
    @RestClient
    RemoteServicesRestClient remoteServicesRestClient;

    @Override
    public Optional<Member> verify(String memberId) {
        Member member = remoteServicesRestClient.verify(memberId);
        return member != null ? Optional.of(member) : Optional.empty();
    }

    @Override
    public Optional<TimeSlot> getAvailableTimeframe(String memberId, String date, String startTime, String endTime) {
        TimeSlot timeSlot = (TimeSlot) remoteServicesRestClient.getAvailableTimeframe(memberId, date, startTime, endTime).getEntity();
        return timeSlot != null ? Optional.of(timeSlot) : Optional.empty();
    }
}
