package at.fhv.matchpoint.partnerservice.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Club", readOnly = true)
public class ClubDTO {

    private String clubId;
    private String clubName;

    private ClubDTO(String clubId, String clubName){
        this.clubId = clubId;
        this.clubName = clubName;
    }

    public static ClubDTO buildDTO(String clubId) {
        return new ClubDTO(clubId, "NOTYETIMPLEMENTEDCLUB");
    }


    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}
