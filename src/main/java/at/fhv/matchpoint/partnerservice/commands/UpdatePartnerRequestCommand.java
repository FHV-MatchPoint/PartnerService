package at.fhv.matchpoint.partnerservice.commands;

import jakarta.validation.constraints.NotNull;

public class UpdatePartnerRequestCommand {

    @NotNull private String memberId;
    @NotNull private String partnerRequestId;
    @NotNull private String date;
    @NotNull private String startTime;
    @NotNull private String endTime;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPartnerRequestId() {
        return partnerRequestId;
    }

    public void setPartnerRequestId(String partnerRequestId) {
        this.partnerRequestId = partnerRequestId;
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
}
