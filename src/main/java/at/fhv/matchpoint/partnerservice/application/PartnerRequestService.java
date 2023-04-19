package at.fhv.matchpoint.partnerservice.application;

import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.UpdatePartnerRequestCommand;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

public interface PartnerRequestService {

    PartnerRequestDTO createPartnerRequest(@Valid CreatePartnerRequestCommand createPartnerRequestCommand);
    PartnerRequestDTO acceptPartnerRequest(@Valid AcceptPartnerRequestCommand acceptPartnerRequestCommand);
    PartnerRequestDTO updatePartnerRequest(@Valid UpdatePartnerRequestCommand updatePartnerRequestCommand);
    PartnerRequestDTO cancelPartnerRequest(@Valid CancelPartnerRequestCommand cancelPartnerRequestCommand);
    PartnerRequestDTO getPartnerRequesById(String memberId, String partnerRequestId);
    List<PartnerRequestDTO> getPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to);
    List<PartnerRequestDTO> getPartnerRequestsByMemberId(String memberId);

}
