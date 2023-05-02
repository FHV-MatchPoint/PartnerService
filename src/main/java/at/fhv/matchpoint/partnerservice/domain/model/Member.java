package at.fhv.matchpoint.partnerservice.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    public String memberId;
    public String clubId;
    public String name;

    public Member(){}

    private Member(String memberId, String clubId, String name) {
        this.memberId = memberId;
        this.clubId = clubId;
        this.name = name;
    }

    public static Member create(String memberId, String clubId, String name) {
        return new Member(memberId, clubId, name);
    }
}
