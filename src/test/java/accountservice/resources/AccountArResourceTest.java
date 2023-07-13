package accountservice.resources;

import accountservice.domain.AccountAr;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class) //tells quarkus to start an H2 db before the test starts
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountArResourceTest {

    @Test
    @Order(1)
    void testRetrieveAll() {
        Response result =
                given()
                        .when().get("/accounts/ar")
                        .then()
                        .statusCode(200)
                        .body(
                                containsString("Debbie Hall"),
                                containsString("David Tennant"),
                                containsString("Alex Kingston")
                        )
                        .extract()
                        .response();
        List<AccountAr> accounts = result.jsonPath().getList("$");

        assertThat(accounts)
                .isNotNull()
                .hasSize(9);
    }

    @Test
    @Order(2)
    void testWithdrawal() {
        AccountAr result =
                given()
                        .body(1)
                        .contentType(ContentType.JSON)
                        .when().put("/accounts/ar/87878787/withdrawal")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(AccountAr.class);
        assertThat(result)
                .isNotNull();
        assertThat(result.balance).isEqualTo(BigDecimal.valueOf(889.54));
    }
}
