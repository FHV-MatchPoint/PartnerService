package at.fhv.matchpoint.partnerservice.unit.application;

import at.fhv.matchpoint.partnerservice.application.dto.ClubDTO;
import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ClubDTOTest {


    final String CLUB_ID = "CLUB_ID";
    final String CLUB_NAME = "NOTYETIMPLEMENTEDCLUB";

    @Test
    public void test_buildDTO() throws DateTimeFormatException {
        ClubDTO clubDTO = ClubDTO.buildDTO(CLUB_ID);
        assertEquals(CLUB_ID, clubDTO.getClubId());
        assertEquals(CLUB_NAME, clubDTO.getClubName());
    }

    @Test
    public void test_setter() throws DateTimeFormatException {
        ClubDTO clubDTO = ClubDTO.buildDTO(CLUB_ID);
        clubDTO.setClubId(CLUB_ID);
        clubDTO.setClubName(CLUB_NAME);
        assertEquals(CLUB_ID, clubDTO.getClubId());
        assertEquals(CLUB_NAME, clubDTO.getClubName());
    }

}