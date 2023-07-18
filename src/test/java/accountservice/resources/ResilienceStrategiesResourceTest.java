package accountservice.resources;

import accountservice.config.WiremockAccountService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
@Slf4j
public class ResilienceStrategiesResourceTest {

    @Test
    public void testBulkheadException() {

        List<CompletionStage<String>> completionStages =
                IntStream.range(1, 3)
                        .mapToObj(this::restCallAsync)
                        .toList();

        completionStages.stream()
                .map(CompletionStage::toCompletableFuture)
                .map(CompletableFuture::join)
                .toList();
    }

    @Test
    public void testFallback() {
        Response response = given()
                .relaxedHTTPSValidation()
                .get("resilience/call-with-fallback/emulate-fallback")
                .then()
                .statusCode(HttpStatus.SC_TOO_MANY_REQUESTS)
                .extract().response();
    }

    @Test
    public void testTimeout() {
        Response response = given()
                .relaxedHTTPSValidation()
                .get("resilience/call-with-timeout")
                .then()
                .statusCode(jakarta.ws.rs.core.Response.Status.GATEWAY_TIMEOUT.getStatusCode())
                .extract().response();
    }

    @Test
    public void testRetry() {
        Response response = given()
                .relaxedHTTPSValidation()
                .get("resilience/call-retry")
                .then()
                .statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
                .extract().response();

        assertThat(new String(response.asByteArray()))
                .isEqualTo("done doesNotDelayAfterRepeatedCalls");
    }


    private CompletionStage<String> restCallAsync(int numberOfCall) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Starting #{} call", numberOfCall);
            int bulkheadLimit = 2;
            int httpStatus = numberOfCall >= bulkheadLimit ? 500 : 200;
            Response response = given()
                    .relaxedHTTPSValidation()
                    .get("resilience/call-bulkhead-method")
                    .then()
                    .statusCode(httpStatus)
                    .extract().response();
            return response + "#" + numberOfCall + 1;
        });
    }
}
