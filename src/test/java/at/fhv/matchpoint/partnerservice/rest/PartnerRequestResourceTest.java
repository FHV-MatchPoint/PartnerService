package at.fhv.matchpoint.partnerservice.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PartnerRequestResourceTest {

    @Test
    public void testCreateEndpoint() {
        given()
                .when().get("partnerRequest/create")
                .then()
                .statusCode(200);
    }

    @Test
    public void testAcceptEndpoint() {
        given()
                .when().get("/partnerRequest/accept")
                .then()
                .statusCode(200);
    }

    @Test
    public void testEventsEndpoint() {
        given()
                .when().get("/partnerRequest/events")
                .then()
                .statusCode(200);
    }

    @Test
    public void testRequestByIdEndpoint() {
        given()
                .when().get("/partnerRequest/1")
                .then()
                .statusCode(200);
    }
}
