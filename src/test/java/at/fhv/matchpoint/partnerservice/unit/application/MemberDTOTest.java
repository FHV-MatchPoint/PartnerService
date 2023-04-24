package at.fhv.matchpoint.partnerservice.unit.application;

import at.fhv.matchpoint.partnerservice.application.dto.MemberDTO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class MemberDTOTest {

    final String MEMBER_ID = "MEMBER_ID";
    final String FIRST_NAME = "NOYET";
    final String LAST_NAME = "IMPLEMENTED";

    @Test
    public void test_buildDTO(){
        MemberDTO memberDTO = MemberDTO.buildDTO(MEMBER_ID);
        assertEquals(MEMBER_ID, memberDTO.getMemberId());
        assertEquals(FIRST_NAME, memberDTO.getFistName());
        assertEquals(LAST_NAME, memberDTO.getLastName());
    }

    @Test
    public void test_setter(){
        MemberDTO memberDTO = MemberDTO.buildDTO("TEMP");
        memberDTO.setMemberId(MEMBER_ID);
        memberDTO.setFistName(FIRST_NAME);
        memberDTO.setLastName(LAST_NAME);
        assertEquals(MEMBER_ID, memberDTO.getMemberId());
        assertEquals(FIRST_NAME, memberDTO.getFistName());
        assertEquals(LAST_NAME, memberDTO.getLastName());
    }
}
