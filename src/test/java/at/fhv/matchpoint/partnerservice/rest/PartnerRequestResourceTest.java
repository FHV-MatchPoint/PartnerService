package at.fhv.matchpoint.partnerservice.rest;

import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PartnerRequestResourceTest {

    @Test
    public void testCreateEndpoint() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate(LocalDate.of(1900, 1, 1).toString());
        initiatePartnerRequestCommand.setStartTime(LocalTime.NOON.toString());
        initiatePartnerRequestCommand.setEndTime(LocalTime.MIDNIGHT.toString());
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest/create")
                .then()
                .statusCode(200);
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
