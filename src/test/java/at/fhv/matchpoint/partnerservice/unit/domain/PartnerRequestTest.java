package at.fhv.matchpoint.partnerservice.unit.domain;

import at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.model.RequestState;
import at.fhv.matchpoint.partnerservice.events.AggregateType;
import at.fhv.matchpoint.partnerservice.events.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.events.RequestInitiatedEvent;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PartnerRequestTest {

    final String AGGREGATE_ID = "Angeln";
    final String OWNER_ID = "Markomannen";
    final String PARTNER_ID = "Helvetier";
    final String CLUD_ID = "Gallier";
    final LocalDate DATE = LocalDate.now();
    final LocalTime START_TIME = LocalTime.NOON;
    final LocalTime END_TIME = LocalTime.MIDNIGHT;

   @Test
   public void testApplyRequestInitiatedEvent() {
       RequestInitiatedEvent event = new RequestInitiatedEvent();
       event.aggregateId = AGGREGATE_ID;
       event.aggregateType = AggregateType.PARTNERREQUEST;
       event.createdAt = LocalDateTime.now();
       event.ownerId = OWNER_ID;
       event.tennisClubId = CLUD_ID;
       event.date = DATE;
       event.startTime = START_TIME;
       event.endTime = END_TIME;
       PartnerRequest partnerRequest = new PartnerRequest();
       partnerRequest.apply(event);
       assertEquals(AGGREGATE_ID, partnerRequest.getPartnerRequestId());
       assertEquals(OWNER_ID, partnerRequest.getOwnerId());
       assertEquals(CLUD_ID, partnerRequest.getClubId());
       assertEquals(DATE, partnerRequest.getDate());
       assertEquals(START_TIME, partnerRequest.getStartTime());
       assertEquals(END_TIME, partnerRequest.getEndTime());
       assertEquals(RequestState.INITIATED, partnerRequest.getState());
   }

   @Test
   public void testApplyRequestAcceptedEvent() {
       RequestAcceptedEvent event = new RequestAcceptedEvent();
       event.aggregateId = AGGREGATE_ID;
       event.aggregateType = AggregateType.PARTNERREQUEST;
       event.createdAt = LocalDateTime.now();
       event.partnerId = PARTNER_ID;
       event.startTime = START_TIME;
       event.endTime = END_TIME;
       PartnerRequest partnerRequest = new PartnerRequest();
       partnerRequest.apply(event);
       assertNull(partnerRequest.getPartnerRequestId());
       assertNull(partnerRequest.getOwnerId());
       assertNull(partnerRequest.getClubId());
       assertNull(partnerRequest.getDate());
       assertEquals(PARTNER_ID, partnerRequest.getPartnerId());
       assertEquals(START_TIME, partnerRequest.getStartTime());
       assertEquals(END_TIME, partnerRequest.getEndTime());
       assertEquals(RequestState.ACCEPTED, partnerRequest.getState());
   }
}
