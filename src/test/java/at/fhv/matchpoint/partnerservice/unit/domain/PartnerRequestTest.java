package at.fhv.matchpoint.partnerservice.unit.domain;

import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.request.*;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.RequestStateChangeException;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PartnerRequestTest {

    final String AGGREGATE_ID = "Angeln";
    final String OWNER_ID = "Markomannen";
    final String PARTNER_ID = "Helvetier";
    final String CLUB_ID = "Gallier";
    final LocalDate DATE = LocalDate.of(1111,11,11);
    final LocalTime START_TIME = LocalTime.NOON;
    final LocalTime END_TIME = LocalTime.MIDNIGHT;

    @Test
    public void test_Process_And_Apply_Valid_InitiatePartnerRequest() throws DateTimeFormatException {
        InitiatePartnerRequestCommand command = new InitiatePartnerRequestCommand();
        command.setClubId(CLUB_ID);
        command.setMemberId(OWNER_ID);
        command.setDate("11-11-1111");
        command.setStartTime("12:00");
        command.setEndTime("00:00");
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestInitiatedEvent event = partnerRequest.process(command);
        partnerRequest.apply(event);
        assertNotNull(partnerRequest.getPartnerRequestId());
        assertEquals(OWNER_ID, partnerRequest.getOwnerId());
        assertEquals(CLUB_ID, partnerRequest.getClubId());
        assertNull(partnerRequest.getPartnerId());
        assertEquals(DATE, partnerRequest.getDate());
        assertEquals(START_TIME, partnerRequest.getStartTime());
        assertEquals(END_TIME, partnerRequest.getEndTime());
        assertEquals(RequestState.INITIATED, partnerRequest.getState());
    }

   @Test
   public void test_Process_And_Apply_Valid_UpdatePartnerRequest() throws DateTimeFormatException, RequestStateChangeException {
        InitiatePartnerRequestCommand command1 = new InitiatePartnerRequestCommand();
        command1.setClubId(CLUB_ID);
        command1.setMemberId(OWNER_ID);
        command1.setDate("11-11-1111");
        command1.setStartTime("12:00");
        command1.setEndTime("00:00");
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestInitiatedEvent event1 = partnerRequest.process(command1);
        partnerRequest.apply(event1);
        RequestOpenedEvent event2 = new RequestOpenedEvent();
        partnerRequest.apply(event2);
        UpdatePartnerRequestCommand command = new UpdatePartnerRequestCommand();
        command.setMemberId(OWNER_ID);
        command.setDate("11-11-1111");
        command.setStartTime("15:00");
        command.setEndTime("18:00");
        RequestUpdatedEvent event = partnerRequest.process(command);
        partnerRequest.apply(event);
        assertNotNull(partnerRequest.getPartnerRequestId());
        assertEquals(OWNER_ID, partnerRequest.getOwnerId());
        assertEquals(CLUB_ID, partnerRequest.getClubId());
        assertNull(partnerRequest.getPartnerId());
        assertEquals(DATE, partnerRequest.getDate());
        assertEquals(LocalTime.of(15,0), partnerRequest.getStartTime());
        assertEquals(LocalTime.of(18,0), partnerRequest.getEndTime());
        assertEquals(RequestState.OPEN, partnerRequest.getState());
   }

   @Test
   public void test_Process_And_Apply_Valid_AcceptPartnerRequest() throws DateTimeFormatException, RequestStateChangeException {
        InitiatePartnerRequestCommand command1 = new InitiatePartnerRequestCommand();
        command1.setClubId(CLUB_ID);
        command1.setMemberId(OWNER_ID);
        command1.setDate("11-11-1111");
        command1.setStartTime("12:00");
        command1.setEndTime("00:00");
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestInitiatedEvent event1 = partnerRequest.process(command1);
        partnerRequest.apply(event1);
        RequestOpenedEvent event2 = new RequestOpenedEvent();
        partnerRequest.apply(event2);
        AcceptPartnerRequestCommand command = new AcceptPartnerRequestCommand();
        command.setPartnerId(PARTNER_ID);
        command.setStartTime("12:00");
        command.setEndTime("18:00");
        RequestAcceptPendingEvent event = partnerRequest.process(command);
        partnerRequest.apply(event);
        assertNotNull(partnerRequest.getPartnerRequestId());
        assertEquals(OWNER_ID, partnerRequest.getOwnerId());
        assertEquals(CLUB_ID, partnerRequest.getClubId());
        assertEquals(PARTNER_ID, partnerRequest.getPartnerId());
        assertEquals(START_TIME, partnerRequest.getStartTime());
        assertEquals(LocalTime.of(0,0), partnerRequest.getEndTime());
        assertEquals(RequestState.ACCEPT_PENDING, partnerRequest.getState());
   }

//    @Test
//    public void test_Process_And_Apply_Valid_CancelPartnerRequest() throws DateTimeFormatException, RequestStateChangeException{
//         InitiatePartnerRequestCommand command = new InitiatePartnerRequestCommand();
//         command.setClubId(CLUB_ID);
//         command.setMemberId(OWNER_ID);
//         command.setDate("11-11-1111");
//         command.setStartTime("12:00");
//         command.setEndTime("00:00");
//         PartnerRequest partnerRequest = new PartnerRequest();
//         RequestInitiatedEvent event = partnerRequest.process(command);
//         partnerRequest.apply(event);
//         CancelPartnerRequestCommand command2 = new CancelPartnerRequestCommand();
//         RequestCancelledEvent event2 = partnerRequest.process(command2);
//         partnerRequest.apply(event2);
//         assertNotNull(partnerRequest.getPartnerRequestId());
//         assertEquals(OWNER_ID, partnerRequest.getOwnerId());
//         assertEquals(CLUB_ID, partnerRequest.getClubId());
//         assertNull(partnerRequest.getPartnerId());
//         assertEquals(DATE, partnerRequest.getDate());
//         assertEquals(START_TIME, partnerRequest.getStartTime());
//         assertEquals(END_TIME, partnerRequest.getEndTime());
//         assertEquals(RequestState.CANCELLED, partnerRequest.getState());    
//     }

//     @Test
//     public void test_Process_Accept_For_Cancelled_PartnerRequest() throws DateTimeFormatException, RequestStateChangeException{
//         InitiatePartnerRequestCommand command = new InitiatePartnerRequestCommand();
//         command.setClubId(CLUB_ID);
//         command.setMemberId(OWNER_ID);
//         command.setDate("11-11-1111");
//         command.setStartTime("12:00");
//         command.setEndTime("00:00");
//         PartnerRequest partnerRequest = new PartnerRequest();
//         RequestInitiatedEvent event = partnerRequest.process(command);
//         partnerRequest.apply(event);
//         CancelPartnerRequestCommand command2 = new CancelPartnerRequestCommand();
//         RequestCancelledEvent event2 = partnerRequest.process(command2);
//         partnerRequest.apply(event2);
//         AcceptPartnerRequestCommand command3 = new AcceptPartnerRequestCommand();
//         command3.setPartnerId(PARTNER_ID);
//         command3.setStartTime("12:00");
//         command3.setEndTime("18:00");
//         assertThrows(RequestStateChangeException.class, () -> {
//         partnerRequest.process(command3);
//         });

//         assertNotNull(partnerRequest.getPartnerRequestId());
//         assertEquals(OWNER_ID, partnerRequest.getOwnerId());
//         assertEquals(CLUB_ID, partnerRequest.getClubId());
//         assertNull(partnerRequest.getPartnerId());
//         assertEquals(DATE, partnerRequest.getDate());
//         assertEquals(START_TIME, partnerRequest.getStartTime());
//         assertEquals(END_TIME, partnerRequest.getEndTime());
//         assertEquals(RequestState.CANCELLED, partnerRequest.getState());
//     }

     @Test
     public void test_Process_Accept_For_Accepted_Pending_PartnerRequest() throws DateTimeFormatException, RequestStateChangeException{
        InitiatePartnerRequestCommand command = new InitiatePartnerRequestCommand();
        command.setClubId(CLUB_ID);
        command.setMemberId(OWNER_ID);
        command.setDate("11-11-1111");
        command.setStartTime("12:00");
        command.setEndTime("00:00");
        PartnerRequest partnerRequest = new PartnerRequest();
        RequestInitiatedEvent event = partnerRequest.process(command);
        partnerRequest.apply(event);
        RequestOpenedEvent event2 = new RequestOpenedEvent();
        partnerRequest.apply(event2);
        AcceptPartnerRequestCommand command2 = new AcceptPartnerRequestCommand();
        command2.setPartnerId(PARTNER_ID);
        command2.setStartTime("12:00");
        command2.setEndTime("18:00");
        RequestAcceptPendingEvent event3 = partnerRequest.process(command2);
        partnerRequest.apply(event3);
        AcceptPartnerRequestCommand command3 = new AcceptPartnerRequestCommand();
        command3.setPartnerId("NEW_PARTNER");
        command3.setStartTime("15:00");
        command3.setEndTime("16:00");
        assertThrows(RequestStateChangeException.class, () -> {
            partnerRequest.process(command3);
        });

        assertNotNull(partnerRequest.getPartnerRequestId());
        assertEquals(OWNER_ID, partnerRequest.getOwnerId());
        assertEquals(CLUB_ID, partnerRequest.getClubId());
        assertEquals(PARTNER_ID, partnerRequest.getPartnerId());
        assertEquals(DATE, partnerRequest.getDate());
        assertEquals(START_TIME, partnerRequest.getStartTime());
        assertEquals(LocalTime.of(0,0), partnerRequest.getEndTime());
        assertEquals(RequestState.ACCEPT_PENDING, partnerRequest.getState());
    }

//     @Test
//     public void test_Process_Update_For_Cancelled_PartnerRequest() throws DateTimeFormatException, RequestStateChangeException{
//        InitiatePartnerRequestCommand command = new InitiatePartnerRequestCommand();
//        command.setClubId(CLUB_ID);
//        command.setMemberId(OWNER_ID);
//        command.setDate("11-11-1111");
//        command.setStartTime("12:00");
//        command.setEndTime("00:00");
//        PartnerRequest partnerRequest = new PartnerRequest();
//        RequestInitiatedEvent event = partnerRequest.process(command);
//        partnerRequest.apply(event);
//        CancelPartnerRequestCommand command2 = new CancelPartnerRequestCommand();
//        RequestCancelledEvent event2 = partnerRequest.process(command2);
//        partnerRequest.apply(event2);
//        UpdatePartnerRequestCommand command3 = new UpdatePartnerRequestCommand();
//        command3.setDate("11-12-1111");
//        command3.setStartTime("15:00");
//        command3.setEndTime("16:00");
//        assertThrows(RequestStateChangeException.class, () -> {
//            partnerRequest.process(command3);
//        });

//        assertNotNull(partnerRequest.getPartnerRequestId());
//        assertEquals(OWNER_ID, partnerRequest.getOwnerId());
//        assertEquals(CLUB_ID, partnerRequest.getClubId());
//        assertNull(partnerRequest.getPartnerId());
//        assertEquals(DATE, partnerRequest.getDate());
//        assertEquals(START_TIME, partnerRequest.getStartTime());
//        assertEquals(END_TIME, partnerRequest.getEndTime());
//        assertEquals(RequestState.CANCELLED, partnerRequest.getState());
//    }

    @Test
    public void test_Process_Update_For_Accepted_PartnerRequest() throws DateTimeFormatException, RequestStateChangeException{
       InitiatePartnerRequestCommand command = new InitiatePartnerRequestCommand();
       command.setClubId(CLUB_ID);
       command.setMemberId(OWNER_ID);
       command.setDate("11-11-1111");
       command.setStartTime("12:00");
       command.setEndTime("00:00");
       PartnerRequest partnerRequest = new PartnerRequest();
       RequestInitiatedEvent event = partnerRequest.process(command);
       partnerRequest.apply(event);
       RequestOpenedEvent event2 = new RequestOpenedEvent();
       partnerRequest.apply(event2);
       AcceptPartnerRequestCommand command2 = new AcceptPartnerRequestCommand();
       command2.setPartnerId(PARTNER_ID);
       command2.setStartTime("12:00");
       command2.setEndTime("18:00");
       RequestAcceptPendingEvent event3 = partnerRequest.process(command2);
       partnerRequest.apply(event3);
       UpdatePartnerRequestCommand command3 = new UpdatePartnerRequestCommand();
       command3.setDate("11-12-1111");
       command3.setStartTime("15:00");
       command3.setEndTime("16:00");
       assertThrows(RequestStateChangeException.class, () -> {
           partnerRequest.process(command3);
       });

       assertNotNull(partnerRequest.getPartnerRequestId());
       assertEquals(OWNER_ID, partnerRequest.getOwnerId());
       assertEquals(CLUB_ID, partnerRequest.getClubId());
       assertEquals(PARTNER_ID, partnerRequest.getPartnerId());
       assertEquals(DATE, partnerRequest.getDate());
       assertEquals(START_TIME, partnerRequest.getStartTime());
       assertEquals(LocalTime.of(0,0), partnerRequest.getEndTime());
       assertEquals(RequestState.ACCEPT_PENDING, partnerRequest.getState());
   }
}
