package at.fhv.matchpoint.partnerservice.rest;

import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.infrastructure.EventRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PartnerRequestResourceTest {

    @Inject
    EventRepository eventRepository;

    @BeforeAll
    public static void clearDatabase(){
        EventRepository eventRepository = new EventRepository();
        eventRepository.deleteAll();
        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void testCreateEndpoint() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(201);
    }

//    @Test
//    public void testAcceptEndpoint() {
//        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
//        given()
//                .header("Content-Type", "application/json")
//                .when().put("/partnerRequest/accept", acceptPartnerRequestCommand)
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    public void TestUpdateEndpoint() {
//        //TODO
//    }
//
//    @Test
//    public void testCancelEndpoint() {
//        //TODO
//    }
//
//    @Test
//    public void testFindByPartnerRequestIdEndpoint(){
//        given()
//                .when().get("/partnerRequest/member/1/1")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    public void testFindByMemberIdEndpoint(){
//        given()
//                .when().get("/partnerRequest/member/1/1")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    public void testEventsEndpoint() {
//        given()
//                .when().get("/partnerRequest/events")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    public void testRequestByIdEndpoint() {
//        given()
//                .when().get("/partnerRequest/1")
//                .then()
//                .statusCode(200);
//    }
}
