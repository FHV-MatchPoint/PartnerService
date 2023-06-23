package at.fhv.matchpoint.partnerservice.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.events.request.*;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.RequestStateChangeException;

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


    public PartnerRequest apply(RequestInitiatedEvent event){
        this.partnerRequestId = event.aggregateId;
        this.ownerId = event.ownerId;
        this.clubId = event.tennisClubId;
        this.date = event.date;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.state = RequestState.INITIATED;
        return this;
    }

    public PartnerRequest apply(RequestOpenedEvent event){
        this.state = RequestState.OPEN;
        return this;
    }

    public PartnerRequest apply(RequestAcceptPendingEvent event){
        this.partnerId = event.partnerId;
        this.state = RequestState.ACCEPT_PENDING;
        return this;
    }

    public PartnerRequest apply(RequestRevertPendingEvent event){
        this.partnerId = null;
        this.state = RequestState.OPEN;
        return this;
    }

    public PartnerRequest apply(RequestAcceptedEvent event){
        this.partnerId = event.partnerId;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.state = RequestState.ACCEPTED;
        return this;
    }

    public PartnerRequest apply(RequestUpdatedEvent event) {
        this.date = event.date;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        return this;
    }

    public PartnerRequest apply(RequestCancelledEvent event) {
        this.state = RequestState.CANCELLED;
        return this;
    }

    public RequestInitiatedEvent process (InitiatePartnerRequestCommand initiatePartnerRequestCommand) throws DateTimeFormatException {
        return RequestInitiatedEvent.create(initiatePartnerRequestCommand);
    }

    public RequestAcceptPendingEvent process (AcceptPartnerRequestCommand acceptPartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException {
        if(this.state.equals(RequestState.OPEN)){
            return RequestAcceptPendingEvent.create(acceptPartnerRequestCommand, this);
        }
        throw new RequestStateChangeException();        
    }

    public RequestUpdatedEvent process (UpdatePartnerRequestCommand updatePartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException {
        if(this.state.equals(RequestState.OPEN)){
            return RequestUpdatedEvent.create(updatePartnerRequestCommand, this);
        }
        throw new RequestStateChangeException();        
    }

    public RequestCancelledEvent process (CancelPartnerRequestCommand cancelPartnerRequestCommand) {
        return RequestCancelledEvent.create(cancelPartnerRequestCommand, this);
    }

    
}
