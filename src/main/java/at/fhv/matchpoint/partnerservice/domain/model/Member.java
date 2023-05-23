package at.fhv.matchpoint.partnerservice.domain.model;

import at.fhv.matchpoint.partnerservice.events.MemberAddedEvent;
import at.fhv.matchpoint.partnerservice.events.MemberLockedEvent;
import at.fhv.matchpoint.partnerservice.events.MemberUnlockedEvent;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    public String memberId;
    public String clubId;
    public String name;
    public Boolean isLocked;

    public Member(){}

    private Member(String memberId, String clubId, String name) {
        this.memberId = memberId;
        this.clubId = clubId;
        this.name = name;
        this.isLocked = false;
    }

    public static Member create(String memberId, String clubId, String name) {
        return new Member(memberId, clubId, name);
    }

    public Member apply(MemberLockedEvent event) {
        this.isLocked = true;
        return this;
    }

    public Member apply(MemberUnlockedEvent event) {
        this.isLocked = false;
        return this;
    }

    public Member apply(MemberAddedEvent event, JsonNode jsonNode) {
        this.memberId = event.entity_id;
        this.clubId = jsonNode.get("tennisClubId").toString();
        this.name = jsonNode.get("name").toString();
        this.isLocked = false;
        return this;
    }
}
