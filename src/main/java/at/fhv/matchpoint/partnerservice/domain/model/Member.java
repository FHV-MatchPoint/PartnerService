package at.fhv.matchpoint.partnerservice.domain.model;

public class Member {

    public String memberId;
    public String clubId;
    public String name;

    private Member(String memberId, String clubId, String name) {
        this.memberId = memberId;
        this.clubId = clubId;
        this.name = name;
    }

    public static Member create(String memberId, String clubId, String name) {
        return new Member(memberId, clubId, name);
    }
}
