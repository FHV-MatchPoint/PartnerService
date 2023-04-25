package at.fhv.matchpoint.partnerservice.commands;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

@Schema(name = "AcceptPartnerRequestCommand", required = true)
public class AcceptPartnerRequestCommand {

    @NotNull private String partnerId;
    @NotNull private String partnerRequestId;
    @NotNull @Schema(format = "HH:mm") private String startTime;
    @NotNull @Schema(format = "HH:mm") private String endTime;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerRequestId() {
        return partnerRequestId;
    }

    public void setPartnerRequestId(String partnerRequestId) {
        this.partnerRequestId = partnerRequestId;
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
