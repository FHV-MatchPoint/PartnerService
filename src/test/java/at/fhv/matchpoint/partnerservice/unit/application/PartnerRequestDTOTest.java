package at.fhv.matchpoint.partnerservice.unit.application;

import at.fhv.matchpoint.partnerservice.application.dto.ClubDTO;
import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.events.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestInitiatedEvent;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class PartnerRequestDTOTest {

    final String MEMBER_ID = "MEMBER_ID";
    final String DATE = "01-01-0001";
    final String START_TIME = "00:00";
    final String END_TIME = "01:00";
    final String CLUB_ID = "CLUB_ID";
    final String PARTNER_ID = "PARTNER_ID";
    final String PARTNER_REQUEST_ID = "PARTNER_REQUEST_ID";
    final String STATE = "STATE";

    @Test
    public void test_buildDTO() throws DateTimeFormatException {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setMemberId(MEMBER_ID);
        initiatePartnerRequestCommand.setDate(DATE);
        initiatePartnerRequestCommand.setStartTime(START_TIME);
        initiatePartnerRequestCommand.setEndTime(END_TIME);
        initiatePartnerRequestCommand.setClubId(CLUB_ID);

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(PARTNER_REQUEST_ID);
        acceptPartnerRequestCommand.setPartnerId(PARTNER_ID);
        acceptPartnerRequestCommand.setStartTime(START_TIME);
        acceptPartnerRequestCommand.setEndTime(END_TIME);

        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.apply(RequestInitiatedEvent.create(initiatePartnerRequestCommand));
        partnerRequest.apply(RequestAcceptedEvent.create(acceptPartnerRequestCommand, partnerRequest));
        PartnerRequestDTO partnerRequestDTO = PartnerRequestDTO.buildDTO(partnerRequest);
        assertEquals(MEMBER_ID, partnerRequestDTO.getOwner());
        assertEquals("0001-01-01", partnerRequestDTO.getDate());
        assertEquals(START_TIME, partnerRequestDTO.getStartTime());
        assertEquals(END_TIME, partnerRequestDTO.getEndTime());
        assertEquals(CLUB_ID, partnerRequestDTO.getClub().getClubId());
        assertEquals(PARTNER_ID, partnerRequestDTO.getPartner());
        assertNotNull(partnerRequestDTO.getPartnerRequestId());
    }

    @Test
    public void test_setter() throws DateTimeFormatException {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setMemberId(MEMBER_ID);
        initiatePartnerRequestCommand.setDate(DATE);
        initiatePartnerRequestCommand.setStartTime(START_TIME);
        initiatePartnerRequestCommand.setEndTime(END_TIME);
        initiatePartnerRequestCommand.setClubId(CLUB_ID);
        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.apply(RequestInitiatedEvent.create(initiatePartnerRequestCommand));

        PartnerRequestDTO partnerRequestDTO = PartnerRequestDTO.buildDTO(partnerRequest);
        partnerRequestDTO.setPartner(PARTNER_ID);
        partnerRequestDTO.setClub(ClubDTO.buildDTO(CLUB_ID));
        partnerRequestDTO.setDate(DATE);
        partnerRequestDTO.setStartTime(START_TIME);
        partnerRequestDTO.setEndTime(END_TIME);
        partnerRequestDTO.setOwner(MEMBER_ID);
        partnerRequestDTO.setPartnerRequestId(PARTNER_REQUEST_ID);
        partnerRequestDTO.setState(STATE);
        assertEquals(MEMBER_ID, partnerRequestDTO.getOwner());
        assertEquals(DATE, partnerRequestDTO.getDate());
        assertEquals(START_TIME, partnerRequestDTO.getStartTime());
        assertEquals(END_TIME, partnerRequestDTO.getEndTime());
        assertEquals(CLUB_ID, partnerRequestDTO.getClub().getClubId());
        assertEquals(PARTNER_ID, partnerRequestDTO.getPartner());
        assertEquals(PARTNER_REQUEST_ID, partnerRequestDTO.getPartnerRequestId());
        assertEquals(STATE, partnerRequestDTO.getState());
    }
}
