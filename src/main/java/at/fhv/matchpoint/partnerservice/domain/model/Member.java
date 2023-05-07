package at.fhv.matchpoint.partnerservice.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    public String memberId;
    public String clubId;
    public Boolean isLocked;

    public Member(){}

    private Member(String memberId, String clubId) {
        this.memberId = memberId;
        this.clubId = clubId;
        this.isLocked = false;
    }

    public static Member create(String memberId, String clubId) {
        return new Member(memberId, clubId);
    }
}
