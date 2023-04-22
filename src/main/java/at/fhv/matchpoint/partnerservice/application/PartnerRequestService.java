package at.fhv.matchpoint.partnerservice.application;

import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;

import java.time.LocalDate;
import java.util.List;

import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.RequestStateChangeException;
import jakarta.validation.Valid;

public interface PartnerRequestService {

    PartnerRequestDTO initiatePartnerRequest(@Valid InitiatePartnerRequestCommand initiatePartnerRequestCommand) throws DateTimeFormatException, MongoDBPersistenceError;
    PartnerRequestDTO acceptPartnerRequest(@Valid AcceptPartnerRequestCommand acceptPartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError;
    PartnerRequestDTO updatePartnerRequest(@Valid UpdatePartnerRequestCommand updatePartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError;
    PartnerRequestDTO cancelPartnerRequest(@Valid CancelPartnerRequestCommand cancelPartnerRequestCommand) throws MongoDBPersistenceError;
    PartnerRequestDTO getPartnerRequestById(String memberId, String partnerRequestId);
    List<PartnerRequestDTO> getOpenPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to);
    List<PartnerRequestDTO> getPartnerRequestsByMemberId(String memberId);

}
