package at.fhv.matchpoint.partnerservice.application.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;

@Schema(name = "PartnerRequest", readOnly = true)
public class PartnerRequestDTO {

    private String partnerRequestId;
    private String owner;
    private String partner;
    private String clubId;
    private String date;
    private String startTime;
    private String endTime;
    private String state;    

    private PartnerRequestDTO(String partnerRequestId, String owner, String partner, String clubId, String date,
            String startTime, String endTime, String state) {
        this.partnerRequestId = partnerRequestId;
        this.owner = owner;
        this.partner = partner;
        this.clubId = clubId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }

    public static PartnerRequestDTO buildDTO(PartnerRequest partnerRequest){
        String owner = partnerRequest.getOwnerId();
        String partner = partnerRequest.getPartnerId();
        return new PartnerRequestDTO(partnerRequest.getPartnerRequestId(), 
        owner, partner,
        partnerRequest.getClubId(), 
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClub(String clubId) {
        this.clubId = clubId;
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

    public static PartnerRequestDTO buildDTO(PartnerRequestReadModel partnerRequest) {
        String owner = partnerRequest.getOwnerId();
        String partner = partnerRequest.getPartnerId();
        return new PartnerRequestDTO(partnerRequest.getPartnerRequestId(), 
        owner, partner,
        partnerRequest.getClubId(), 
        partnerRequest.getDate().toString(),
        partnerRequest.getStartTime().toString(), 
        partnerRequest.getEndTime().toString(), 
        partnerRequest.getState().toString());
    }

    

    
    
}
