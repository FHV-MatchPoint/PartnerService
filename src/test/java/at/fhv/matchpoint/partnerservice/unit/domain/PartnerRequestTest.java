package at.fhv.matchpoint.partnerservice.unit.domain;

import at.fhv.matchpoint.partnerservice.domain.PartnerRequest;
import at.fhv.matchpoint.partnerservice.domain.RequestState;
import at.fhv.matchpoint.partnerservice.event.RequestAcceptedEvent;
import at.fhv.matchpoint.partnerservice.event.RequestCreatedEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class PartnerRequestTest {

    @Test
    public void testApplyRequestCreatedEvent() {
        RequestCreatedEvent event = new RequestCreatedEvent();
        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.apply(event);
        assertEquals("1", partnerRequest.getPartnerRequestId());
        assertEquals("Markomannen", partnerRequest.getOwnerId());
        assertEquals("Panther", partnerRequest.getClubId());
        assertEquals(LocalDate.now(), partnerRequest.getDate());
        assertEquals(LocalTime.NOON, partnerRequest.getStartTime());
        assertEquals(LocalTime.MIDNIGHT, partnerRequest.getEndTime());
        assertEquals(RequestState.CREATED, partnerRequest.getState());
    }

    @Test
    public void testApplyRequestAcceptedEvent() {
        RequestAcceptedEvent event = new RequestAcceptedEvent();
        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.apply(event);
        assertEquals("Helvetier", partnerRequest.getPartnerId());
        assertNotNull(partnerRequest.getStartTime());
        assertEquals(LocalTime.MIDNIGHT, partnerRequest.getEndTime());
        assertEquals(RequestState.ACCEPTED, partnerRequest.getState());
    }
}
