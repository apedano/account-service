package accountservice.resources;

import accountservice.domain.AccountAr;
import accountservice.domain.AccountR;
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
public class AccountRepositoryResourceTest {

    @Test
    @Order(1)
    void testRetrieveAll() {
        Response result =
                given()
                        .when().get("/accounts/repo/")
                        .then()
                        .statusCode(200)
                        .body(
                                containsString("Debbie Hall"),
                                containsString("David Tennant"),
                                containsString("Alex Kingston")
                        )
                        .extract()
                        .response();
        List<AccountR> accounts = result.jsonPath().getList("$");

        assertThat(accounts)
                .isNotNull()
                .hasSize(8);
    }

    @Test
    @Order(2)
    void testWithdrawal() {
        AccountR result =
                given()
                        .body(1)
                        .contentType(ContentType.JSON)
                        .when().put("/accounts/repo/87878787/withdrawal")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(AccountR.class);
        assertThat(result)
                .isNotNull();
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(889.54));
    }
}
