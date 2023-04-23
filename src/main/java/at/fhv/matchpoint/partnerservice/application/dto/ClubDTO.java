package at.fhv.matchpoint.partnerservice.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Club", readOnly = true)
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
