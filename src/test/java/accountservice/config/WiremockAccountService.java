package accountservice.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import groovy.util.logging.Slf4j;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.smallrye.config.ConfigLogging.log;

/**
 * Implementing @QuarkusTestResourceLifecycleManager allows to change the start() and end() methods
 * to any test annotated with @QuarkusTestResource
 */
@Slf4j
public class WiremockAccountService implements QuarkusTestResourceLifecycleManager {
    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        log.info("Starting wireMockServer");
        wireMockServer.start();
        stubFor(get(urlEqualTo("/resilience/call-with-timeout"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(200) //this will trigger a timetout
                        .withStatus(200)
                ));
        return Collections.singletonMap(
                "quarkus.rest-client.account-service.url", //@RegisterRestClient config key based
//                "banking.api.service.AccountService/mp-rest/url", //MicroProfile Rest client based config also valid
                wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}
