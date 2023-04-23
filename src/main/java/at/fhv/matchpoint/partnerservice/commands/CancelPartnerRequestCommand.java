package at.fhv.matchpoint.partnerservice.commands;

import jakarta.validation.constraints.NotNull;

public class CancelPartnerRequestCommand {

    @NotNull private String memberId;
    @NotNull private String partnerRequestId;

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
}
