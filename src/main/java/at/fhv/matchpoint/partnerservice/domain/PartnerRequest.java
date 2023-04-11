package at.fhv.matchpoint.partnerservice.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class PartnerRequest {

    private String partnerRequestId;
    private String ownerId;
    private String parnterId;
    private String clubId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RequestState state;

    public PartnerRequest() {
    }

    private PartnerRequest(String partnerRequestId, String ownerId, String parnterId, String clubId, LocalDate date,
            LocalTime startTime, LocalTime endTime, RequestState state) {
        this.partnerRequestId = partnerRequestId;
        this.ownerId = ownerId;
        this.parnterId = parnterId;
        this.clubId = clubId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }

    public static PartnerRequest create(String partnerRequestId, String ownerId, String parnterId, String clubId, LocalDate date,
    LocalTime startTime, LocalTime endTime, RequestState state){
        return new PartnerRequest(partnerRequestId, ownerId, parnterId, clubId, date, startTime, endTime, state);
    }

    //TODO apply methods

    

    
    
}
