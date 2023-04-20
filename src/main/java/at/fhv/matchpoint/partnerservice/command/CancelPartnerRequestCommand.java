package at.fhv.matchpoint.partnerservice.command;

public class CancelPartnerRequestCommand {

    private String memberId;
    private String partnerRequestId;

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
