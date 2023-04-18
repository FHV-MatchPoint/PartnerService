package at.fhv.matchpoint.partnerservice.application;

import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequest;
import at.fhv.matchpoint.partnerservice.event.Event;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

public interface PartnerRequestService {

    PartnerRequest createPartnerRequest(@Valid CreatePartnerRequestCommand createPartnerRequestCommand);
    PartnerRequest acceptPartnerRequest(@Valid AcceptPartnerRequestCommand acceptPartnerRequestCommand);
    PartnerRequest updatePartnerRequest(@Valid UpdatePartnerRequestCommand updatePartnerRequestCommand);
    PartnerRequest cancelPartnerRequest(@Valid CancelPartnerRequestCommand cancelPartnerRequestCommand);
    List<Event> getEvents(String memberId, String clubId, LocalDate from, LocalDate to);

}
