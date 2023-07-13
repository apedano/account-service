//package accountservice.health;
//
//import banking.api.service.AccountService;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import jakarta.ws.rs.WebApplicationException;
//import org.eclipse.microprofile.health.HealthCheck;
//import org.eclipse.microprofile.health.HealthCheckResponse;
//import org.eclipse.microprofile.health.Readiness;
//import org.eclipse.microprofile.rest.client.inject.RestClient;
//
//import java.math.BigDecimal;
//
//@Readiness
//@ApplicationScoped
//public class AccountHealthReadinessCheck implements HealthCheck {
//
//    @Inject
//    @RestClient
//    private AccountService accountService;
//
//    @Override
//    public HealthCheckResponse call() {
//        BigDecimal balance = BigDecimal.valueOf(Integer.MIN_VALUE);
//        try {
//            balance = accountService.getBalance(999999999L);
//        } catch (WebApplicationException ex) {
//            if (ex.getResponse().getStatus() >= 500) {
//                return HealthCheckResponse
//                        .named("AccountServiceCheck")
//                        .withData("exception", ex.toString())
//                        .down()
//                        .build();
//            }
//        }
//        return HealthCheckResponse
//                .named("AccountServiceCheck")
//                .withData("balance", balance.toString())
//                .up()
//                .build();
//    }
//
//}
//
