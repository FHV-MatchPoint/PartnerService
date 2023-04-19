package at.fhv.matchpoint.partnerservice.application.dto;

public class ClubDTO {

    public String clubId;
    public String clubName;

    private ClubDTO(String clubId, String clubName){
        this.clubId = clubId;
        this.clubName = clubName;
    }

    public static ClubDTO buildDTO(String clubId) {
        return new ClubDTO(clubId, "NOTYETIMPLEMENTEDCLUB");
    }

}
