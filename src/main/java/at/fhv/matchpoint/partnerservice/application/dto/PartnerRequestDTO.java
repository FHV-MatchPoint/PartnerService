package at.fhv.matchpoint.partnerservice.application.dto;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;

public class PartnerRequestDTO {

    private String partnerRequestId;
    private MemberDTO owner;
    private MemberDTO partner;
    private ClubDTO club;
    private String date;
    private String startTime;
    private String endTime;
    private String state;    

    private PartnerRequestDTO(String partnerRequestId, MemberDTO owner, MemberDTO partner, ClubDTO club, String date,
            String startTime, String endTime, String state) {
        this.partnerRequestId = partnerRequestId;
        this.owner = owner;
        this.partner = partner;
        this.club = club;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }

    public static PartnerRequestDTO buildDTO(PartnerRequest partnerRequest){
        MemberDTO owner = MemberDTO.buildDTO(partnerRequest.getOwnerId());
        MemberDTO partner = MemberDTO.buildDTO(partnerRequest.getPartnerId());
        ClubDTO club = ClubDTO.buildDTO(partnerRequest.getClubId());
        return new PartnerRequestDTO(partnerRequest.getPartnerRequestId(), 
        owner, partner, club, 
        partnerRequest.getDate().toString(),
        partnerRequest.getStartTime().toString(), 
        partnerRequest.getEndTime().toString(), 
        partnerRequest.getState().toString());
    }

    public String getPartnerRequestId() {
        return partnerRequestId;
    }

    public void setPartnerRequestId(String partnerRequestId) {
        this.partnerRequestId = partnerRequestId;
    }

    public MemberDTO getOwner() {
        return owner;
    }

    public void setOwner(MemberDTO owner) {
        this.owner = owner;
    }

    public MemberDTO getPartner() {
        return partner;
    }

    public void setPartner(MemberDTO partner) {
        this.partner = partner;
    }

    public ClubDTO getClub() {
        return club;
    }

    public void setClub(ClubDTO club) {
        this.club = club;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    

    
    
}
