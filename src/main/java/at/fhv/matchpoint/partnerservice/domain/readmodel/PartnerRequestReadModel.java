package at.fhv.matchpoint.partnerservice.domain.readmodel;

import java.time.LocalDate;
import java.time.LocalTime;

import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.request.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PartnerRequestReadModel {

    @Id
    private String partnerRequestId;
    
    private String ownerId;
    private String partnerId;
    private String clubId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RequestState state;

    public PartnerRequestReadModel() {
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


    public PartnerRequestReadModel apply(RequestInitiatedEvent event){
        this.partnerRequestId = event.aggregateId;
        this.ownerId = event.ownerId;
        this.clubId = event.tennisClubId;
        this.date = event.date;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.state = RequestState.INITIATED;
        return this;
    }

    public PartnerRequestReadModel apply(RequestOpenedEvent event){
        this.state = RequestState.OPEN;
        return this;
    }

    public PartnerRequestReadModel apply(RequestAcceptPendingEvent event){
        this.partnerId = event.partnerId;
        this.state = RequestState.ACCEPT_PENDING;
        return this;
    }

    public PartnerRequestReadModel apply(RequestRevertPendingEvent event){
        this.partnerId = null;
        this.state = RequestState.OPEN;
        return this;
    }

    public PartnerRequestReadModel apply(RequestAcceptedEvent event){
        this.partnerId = event.partnerId;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        this.state = RequestState.ACCEPTED;
        return this;
    }

    public PartnerRequestReadModel apply(RequestUpdatedEvent event) {
        this.date = event.date;
        this.startTime = event.startTime;
        this.endTime = event.endTime;
        return this;
    }

    public PartnerRequestReadModel apply(RequestCancelledEvent event) {
        this.state = RequestState.CANCELLED;
        return this;
    }

}
