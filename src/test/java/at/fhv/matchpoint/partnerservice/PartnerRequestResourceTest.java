package at.fhv.matchpoint.partnerservice;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PartnerRequestResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().put("/partnerRequest/memberId/markomannen")
          .then()
             .statusCode(200)
             .body(is("This should be handled with asynchronous messaging. Therefore this endpoint will only return this string. USE REDIS STREAMS!\n\nWe recommend Kafka though"));
    }

}