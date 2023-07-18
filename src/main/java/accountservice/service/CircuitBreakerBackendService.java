package accountservice.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static accountservice.service.CircuitBreakerBackendService.Outcome.FAILURE;
import static accountservice.service.CircuitBreakerBackendService.Outcome.SUCCESS;

/**
 * This class resambles the behavior described in the 'How a circuit breaker works' paragraph's picture
 */
@ApplicationScoped
@Slf4j
public class CircuitBreakerBackendService {

    private static final List<Outcome> OUTCOME_SEQUENCE = Arrays.asList(SUCCESS, FAILURE, FAILURE, FAILURE, FAILURE, SUCCESS, SUCCESS, SUCCESS);

    private final AtomicInteger atomicInteger = new AtomicInteger();

    public Outcome callService() {
        log.info("Called CircuitBreakerBackendService at {}", new Date());
        Outcome outcome = OUTCOME_SEQUENCE.get(atomicInteger.getAndIncrement());
        if(atomicInteger.get() == OUTCOME_SEQUENCE.size()) {
            atomicInteger.set(0);
        }
        if(outcome.equals(FAILURE)) {
            throw new WebApplicationException("Exception due to failure outcome");
        }
        return outcome;
    }


    public enum Outcome {
        SUCCESS, FAILURE
    }

}
