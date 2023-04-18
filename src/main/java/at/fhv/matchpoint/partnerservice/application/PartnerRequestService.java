package at.fhv.matchpoint.partnerservice.application;

import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.PartnerRequest;
import at.fhv.matchpoint.partnerservice.event.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PartnerRequestService {

    PartnerRequest createPartnerRequest(CreatePartnerRequestCommand createPartnerRequestCommand);
    PartnerRequest acceptPartnerRequest(AcceptPartnerRequestCommand acceptPartnerRequestCommand);
    PartnerRequest updatePartnerRequest(UpdatePartnerRequestCommand updatePartnerRequestCommand);
    PartnerRequest cancelPartnerRequest(CancelPartnerRequestCommand cancelPartnerRequestCommand);
    List<Event> getEvents(String memberId, String clubId, LocalDate from, LocalDate to);

}
