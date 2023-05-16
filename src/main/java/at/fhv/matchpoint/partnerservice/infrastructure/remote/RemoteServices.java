package at.fhv.matchpoint.partnerservice.infrastructure.remote;

import java.util.Optional;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import at.fhv.matchpoint.partnerservice.domain.model.TimeSlot;

public interface RemoteServices {

    Optional<Member> verify(String memberId);

    Optional<TimeSlot> getAvailableTimeframe(String memberId,
                                   String date,
                                   String startTime,
                                   String endTime);
    
}
