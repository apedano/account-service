package accountservice.resources;

import accountservice.domain.Account;
import accountservice.domain.AccountStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static accountservice.resources.AccountResource.ACCOUNT1;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AccountResourceTest {

    @Inject
    Jsonb jsonb; //this comes from quarkus-rest-client-jsonb extension

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
        assertThat(accounts, hasSize(3));
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
        assertThat(account.getAccountNumber(), equalTo(3L));
        assertThat(account.getCustomerName(), equalTo("Customer2"));
        assertThat(account.getBalance(), equalTo(new BigDecimal("12.122")));
        assertThat(account.getAccountStatus(), equalTo(AccountStatus.OPEN));
    }

    @Test
    void createAccount() {
        Account newAccount = new Account(6L, 9L, "Customer9", BigDecimal.valueOf(123.333));
        Account returnedAccount =
                given()
                        .contentType(ContentType.JSON)
                .body(jsonb.toJson(newAccount))
                .when().post("/accounts")
                .then()
                .statusCode(201)
                .extract()
                .as(Account.class);
        assertThat(returnedAccount, equalTo(newAccount));

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
        assertThat(accounts, hasSize(4));
    }

    @Test
    void deleteAccount() {
        Account toDelete = ACCOUNT1;
        Response result =
                given()
                        .contentType(ContentType.JSON)
                        .body(jsonb.toJson(toDelete))
                        .when().delete("/accounts")
                        .then()
                        .body(
                                containsString("Customer2"),
                                not(containsString(toDelete.getCustomerName()))
                        )
                        .statusCode(200)
                        .extract().response();
        List<Account> accounts = result.jsonPath().getList("$");
        assertThat(accounts, hasSize(2));

    }


}