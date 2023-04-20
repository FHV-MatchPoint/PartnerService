package at.fhv.matchpoint.partnerservice.command;

import java.time.LocalDate;
import java.time.LocalTime;

public class AcceptPartnerRequestCommand {
    private String partnerId;
    private String partnerRequestId;
    private LocalTime startTime;
    private LocalTime endTime;

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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
