package at.fhv.matchpoint.partnerservice.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.event.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;

public class PartnerRequest {

    private String partnerRequestId;
    private String ownerId;
    private String partnerId;
    private String clubId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RequestState state;

    public PartnerRequest() {
    }

    public String getPartnerRequestId() {
        return partnerRequestId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getClubId() {
        return clubId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public RequestState getState() {
        return state;
    }

    //TODO apply methods

    public PartnerRequest apply(RequestCreatedEvent event){
        this.partnerRequestId = event.aggregateId;
        this.ownerId = event.ownerId;
        this.clubId = event.tennisClubId;
        this.date = event.date;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.state = RequestState.CREATED;
        return this;
    }

    public PartnerRequest apply(RequestAcceptedEvent event){
        this.partnerId = event.partnerId;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.state = RequestState.ACCEPTED;
        return this;
    }

    public RequestCreatedEvent process (CreatePartnerRequestCommand createPartnerRequestCommand) {
        return null;
    }
    
}
