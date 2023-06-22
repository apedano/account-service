package accountservice.resources;

import accountservice.domain.Account;
import accountservice.domain.AccountStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperSerializationContext;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

import static accountservice.resources.AccountResource.ACCOUNT1;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class AccountResourceTest {

    @Inject
    ObjectMapper objectMapper; // = new GsonMapper(new DefaultGsonObjectMapperFactory());

    @Inject
    AccountResource accountResource;

    @BeforeEach
    void resetAccounts() {
        //the Resource by default is Singleton scoped. I don't want to change the scope of the bean
        accountResource.initialise();
    }

    @Test
    void allAccountsRetrieved() {
        Response result =
                given()
                        .when().get("/accounts")
                        .then()
                        .statusCode(200)
                        .body(
                                containsString("Customer1"),
                                containsString("Customer2")
                        )
                        .extract()
                        .response();
        List<Account> accounts = result.jsonPath().getList("$");
        assertThat(accounts)
                .isNotNull()
                .hasSize(3);
    }

    @Test
    void singleAccountRetrieved() {
        Account account =
                given()
                        .when().get("/accounts/{accountNumber}", 3)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(Account.class);
        assertThat(account)
                .extracting(Account::getAccountNumber, Account::getCustomerName, Account::getBalance, Account::getAccountStatus)
                .containsExactly(3L, "Customer2", new BigDecimal("12.122"), AccountStatus.OPEN);
    }

    @Test
    void createAccount() throws JsonProcessingException {
        Account newAccount = new Account(6L, 9L, "Customer9", BigDecimal.valueOf(123.333));
        Account returnedAccount =
                given()
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(newAccount))
                        .when().post("/accounts")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(Account.class);
        assertThat(returnedAccount).isEqualTo(newAccount);

        Response result =
                given()
                        .when().get("/accounts")
                        .then()
                        .statusCode(200)
                        .body(
                                containsString("Customer2"),
                                containsString(newAccount.getCustomerName())
                        )
                        .extract()
                        .response();
        List<Account> accounts = result.jsonPath().getList("$");
        assertThat(accounts).isNotNull().hasSize(4);
    }

    @Test
    void deleteAccount() throws JsonProcessingException {
        Account toDelete = ACCOUNT1;
        Response result =
                given()
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(toDelete))
                        .when().delete("/accounts")
                        .then()
                        .body(
                                containsString("Customer2"),
                                not(containsString(toDelete.getCustomerName()))
                        )
                        .statusCode(200)
                        .extract().response();
        List<Account> accounts = result.jsonPath().getList("$");
        assertThat(accounts).isNotNull().hasSize(2);

    }

}