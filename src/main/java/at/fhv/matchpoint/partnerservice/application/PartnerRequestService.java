package at.fhv.matchpoint.partnerservice.application;

import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;

import java.time.LocalDate;
import java.util.List;

import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotAuthorizedException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MongoDBPersistenceError;
import at.fhv.matchpoint.partnerservice.utils.exceptions.PartnerRequestNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.RequestStateChangeException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.VersionNotMatchingException;
import jakarta.validation.Valid;

public interface PartnerRequestService {

    PartnerRequestDTO initiatePartnerRequest(@Valid InitiatePartnerRequestCommand initiatePartnerRequestCommand) throws DateTimeFormatException, MongoDBPersistenceError, MemberNotAuthorizedException;
    PartnerRequestDTO acceptPartnerRequest(@Valid AcceptPartnerRequestCommand acceptPartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException, MemberNotAuthorizedException;
    PartnerRequestDTO updatePartnerRequest(@Valid UpdatePartnerRequestCommand updatePartnerRequestCommand) throws DateTimeFormatException, RequestStateChangeException, MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException, MemberNotAuthorizedException;
    PartnerRequestDTO cancelPartnerRequest(@Valid CancelPartnerRequestCommand cancelPartnerRequestCommand) throws MongoDBPersistenceError, VersionNotMatchingException, PartnerRequestNotFoundException, MemberNotAuthorizedException, RequestStateChangeException;
    PartnerRequestDTO getPartnerRequestById(String memberId, String partnerRequestId) throws PartnerRequestNotFoundException, MemberNotAuthorizedException;
    List<PartnerRequestDTO> getOpenPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to) throws MemberNotAuthorizedException;
    List<PartnerRequestDTO> getPartnerRequestsByMemberId(String memberId) throws MemberNotAuthorizedException;

}
