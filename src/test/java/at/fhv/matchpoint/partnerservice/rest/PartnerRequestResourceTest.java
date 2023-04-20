package at.fhv.matchpoint.partnerservice.rest;

import at.fhv.matchpoint.partnerservice.command.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.command.CreatePartnerRequestCommand;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PartnerRequestResourceTest {

    @Test
    public void testCreateEndpoint() {
        CreatePartnerRequestCommand createPartnerRequestCommand = new CreatePartnerRequestCommand();
        createPartnerRequestCommand.setClubId("TestClub");
        createPartnerRequestCommand.setMemberId("TestMember");
        createPartnerRequestCommand.setDate(LocalDate.of(1900, 1, 1).toString());
        createPartnerRequestCommand.setStartTime(LocalTime.NOON.toString());
        createPartnerRequestCommand.setEndTime(LocalTime.MIDNIGHT.toString());
        given()
                .header("Content-Type", "application/json")
                .body(createPartnerRequestCommand)
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
