package at.fhv.matchpoint.partnerservice.rest;

import at.fhv.matchpoint.partnerservice.application.dto.PartnerRequestDTO;
import at.fhv.matchpoint.partnerservice.commands.AcceptPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.CancelPartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.InitiatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.commands.UpdatePartnerRequestCommand;
import at.fhv.matchpoint.partnerservice.infrastructure.EventRepository;
import at.fhv.matchpoint.partnerservice.infrastructure.secretquarkuslaborwherethemagicismade.AlexAndJustinIgnoreThis;
import at.fhv.matchpoint.partnerservice.infrastructure.secretquarkuslaborwherethemagicismade.DontLookAtThis;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PartnerRequestResourceTest {

    @Inject
    PartnerRequestResource api;

    @Inject
    EventRepository eventRepository;

    @Inject
    DontLookAtThis mockBean;

    // for later so that we now how to setup mocks
    @BeforeAll
    public static void setup(){
        AlexAndJustinIgnoreThis mock = Mockito.mock(AlexAndJustinIgnoreThis.class);
        Mockito.when(mock.iSaidIgnoreThisAndStopLookingAtThisFunctionItIsJustSomeQuarkusMagic()).thenReturn(true);
        QuarkusMock.installMockForType(mock, DontLookAtThis.class);        
    }

    @BeforeEach
    public void clearDatabase(){
        eventRepository.deleteAll();
    }

    /*****************************
     *                           *
     *  INITIATE ENDPOINT TESTS  *
     *                           *
     *****************************/

    @Test
    public void test_initaite_valid_PartnerRequest() {
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

        assertEquals(1, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_missing_memberId_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_missing_clubId_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_missing_date_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_missing_startTime_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setEndTime("21:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_missing_endTime_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_wrong_date_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("2020/01/01");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(422);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_wrong_startTime_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00:15");
        initiatePartnerRequestCommand.setEndTime("21:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(422);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_initaite_wrong_endTime_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("1:00");
        given()
                .header("Content-Type", "application/json")
                .body(initiatePartnerRequestCommand)
                .when().post("/partnerRequest")
                .then()
                .statusCode(422);

        assertEquals(0, eventRepository.listAll().size());
    }

    /***************************
     *                         *
     *  ACCEPT ENDPOINT TESTS  *
     *                         *
     ***************************/

    @Test
    public void test_accept_valid_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(200);

        assertEquals(2, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_already_accepted_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        api.accept(acceptPartnerRequestCommand);

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(412);

        assertEquals(2, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_already_cancelled_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        CancelPartnerRequestCommand cancelPartnerRequestCommand = new CancelPartnerRequestCommand();
        cancelPartnerRequestCommand.setMemberId("TestMember");
        cancelPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());

        api.cancel(cancelPartnerRequestCommand);

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(412);

        assertEquals(2, eventRepository.listAll().size());
    }

    //TODO implement solution and fix test
    //@Test
    public void test_accept_own_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("TestMember");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(200);

        assertEquals(1, eventRepository.listAll().size());
    }

    //TODO implement solution and fix test
    //@Test
    public void test_accept_foreign_club_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("MemberNotOfTestClub");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(200);

        assertEquals(1, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_unknown_PartnerRequest() {
        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId("RANDOMORNOTKOWNID");
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(404);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_missing_partnerRequestId_PartnerRequest() {
        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_missing_partnerId_PartnerRequest() {
        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId("RANDOMORNOTKOWNID");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_missing_startTime_PartnerRequest() {
        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId("RANDOMORNOTKOWNID");
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_missing_endTime_PartnerRequest() {
        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId("RANDOMORNOTKOWNID");
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_wrong_startTime_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20-00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(422);

        assertEquals(1, eventRepository.listAll().size());
    }

    @Test
    public void test_accept_wrong_endTime_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20-00");
        acceptPartnerRequestCommand.setEndTime("21:70");

        given()
                .header("Content-Type", "application/json")
                .body(acceptPartnerRequestCommand)
                .when().put("/partnerRequest/accept")
                .then()
                .statusCode(422);

        assertEquals(1, eventRepository.listAll().size());
    }

    /*****************************
     *                           *
     *   UPDATE ENDPOINT TESTS   *
     *                           *
     *****************************/

    @Test
    public void test_update_valid_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(200);

        assertEquals(2, eventRepository.listAll().size());
    }

    @Test
    public void test_update_already_updated_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2020");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        api.update(updatePartnerRequestCommand);

        updatePartnerRequestCommand.setDate("02-02-2020");
        updatePartnerRequestCommand.setStartTime("16:00");
        updatePartnerRequestCommand.setEndTime("18:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(200);

        assertEquals(3, eventRepository.listAll().size());
    }

    @Test
    public void test_update_accepted_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        AcceptPartnerRequestCommand acceptPartnerRequestCommand = new AcceptPartnerRequestCommand();
        acceptPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        acceptPartnerRequestCommand.setPartnerId("TestPartner");
        acceptPartnerRequestCommand.setStartTime("20:00");
        acceptPartnerRequestCommand.setEndTime("21:00");

        api.accept(acceptPartnerRequestCommand);

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2020");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(412);

        assertEquals(2, eventRepository.listAll().size());
    }

    @Test
    public void test_update_cancelled_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        CancelPartnerRequestCommand cancelPartnerRequestCommand = new CancelPartnerRequestCommand();
        cancelPartnerRequestCommand.setMemberId("TestMember");
        cancelPartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());

        api.cancel(cancelPartnerRequestCommand);

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2020");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(412);

        assertEquals(2, eventRepository.listAll().size());
    }

    //TODO implement and fix test
    //@Test
    public void test_update_foreign_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("AnotherMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(404);

        assertEquals(1, eventRepository.listAll().size());
    }

    @Test
    public void test_update_unknown_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        api.create(initiatePartnerRequestCommand).getEntity();

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId("NOTAREALID");
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(404);

        assertEquals(1, eventRepository.listAll().size());
    }

    @Test
    public void test_update_missing_partnerRequestId_PartnerRequest() {
        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_update_missing_memberId_PartnerRequest() {
        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId("NOTAREALIDBUTNOTNEEDEDFORTHISTEST");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_update_missing_date_PartnerRequest() {
        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId("NOTAREALIDBUTNOTNEEDEDFORTHISTEST");
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_update_missing_startTime_PartnerRequest() {
        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId("NOTAREALIDBUTNOTNEEDEDFORTHISTEST");
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_update_missing_endTime_PartnerRequest() {
        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId("NOTAREALIDBUTNOTNEEDEDFORTHISTEST");
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("15:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(400);

        assertEquals(0, eventRepository.listAll().size());
    }

    @Test
    public void test_update_wrong_date_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("12-24-2021");
        updatePartnerRequestCommand.setStartTime("15:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(422);

        assertEquals(1, eventRepository.listAll().size());
    }

    @Test
    public void test_update_wrong_startTime_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("26:00");
        updatePartnerRequestCommand.setEndTime("21:00");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(422);

        assertEquals(1, eventRepository.listAll().size());
    }

    @Test
    public void test_update_wrong_endTime_format_PartnerRequest() {
        InitiatePartnerRequestCommand initiatePartnerRequestCommand = new InitiatePartnerRequestCommand();
        initiatePartnerRequestCommand.setClubId("TestClub");
        initiatePartnerRequestCommand.setMemberId("TestMember");
        initiatePartnerRequestCommand.setDate("01-01-2020");
        initiatePartnerRequestCommand.setStartTime("20:00");
        initiatePartnerRequestCommand.setEndTime("21:00");

        PartnerRequestDTO dto = (PartnerRequestDTO) api.create(initiatePartnerRequestCommand).getEntity();

        UpdatePartnerRequestCommand updatePartnerRequestCommand = new UpdatePartnerRequestCommand();
        updatePartnerRequestCommand.setPartnerRequestId(dto.getPartnerRequestId());
        updatePartnerRequestCommand.setMemberId("TestMember");
        updatePartnerRequestCommand.setDate("01-01-2021");
        updatePartnerRequestCommand.setStartTime("20:00");
        updatePartnerRequestCommand.setEndTime("21:00:13:1231");

        given()
                .header("Content-Type", "application/json")
                .body(updatePartnerRequestCommand)
                .when().put("/partnerRequest/update")
                .then()
                .statusCode(422);

        assertEquals(1, eventRepository.listAll().size());
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
