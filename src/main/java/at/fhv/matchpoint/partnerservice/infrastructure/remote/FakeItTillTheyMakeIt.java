package at.fhv.matchpoint.partnerservice.infrastructure.remote;

import java.util.Optional;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.domain.model.TimeSlot;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@DefaultBean
public class FakeItTillTheyMakeIt implements RemoteServices {

    @Override
    public Optional<Member> verify(String memberId) {
        if(!memberId.equals("TestMember") && !memberId.equals("TestPartner")){
            memberId = null;
        } 
        return memberId != null ? Optional.of(Member.create(memberId, "TestClub", "Reiner Funden")) : Optional.empty();
    }

    @Override
    public Optional<TimeSlot> getAvailableTimeframe(String memberId, String date, String startTime, String endTime) {
        return Optional.empty();
    }
}
