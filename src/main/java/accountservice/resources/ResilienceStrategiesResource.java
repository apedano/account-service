package accountservice.resources;

import accountservice.service.CircuitBreakerBackendService;
import accountservice.service.ServiceForRetry;
import accountservice.service.VerySlowServiceCaller;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.faulttolerance.exceptions.BulkheadException;import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

import java.time.temporal.ChronoUnit;

@Path("resilience")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ResilienceStrategiesResource {

    @Inject
    private VerySlowServiceCaller verySlowServiceCaller;

    @Inject
    private ServiceForRetry serviceForRetry;

    @Inject
    private CircuitBreakerBackendService circuitBreakerBackendService;

    @GET
    @Path("/call-bulkhead-method")
    @Bulkhead(value = 1)
    public Response callBulkheadMethod() {
        return Response.ok().entity(this.verySlowServiceCaller.doCallResponseAfter10Sec()).build();
    }

    @GET
    @Path("/call-with-fallback/{inputCode}")
    @Bulkhead(value = 1)
    @Fallback(fallbackMethod = "bulkheadFallback", //Method name of the fallback impl with the same signature as the resource method
            applyOn = { BulkheadException.class })
    public Response exampleWithFallbackMethod(@PathParam("inputCode") String inputCode) {
        if("emulate-fallback".equals(inputCode)) { //this is usually not needed
            throw new BulkheadException("Emulated bulkhead exception");
        }
        return Response.ok().entity(this.verySlowServiceCaller.doCallResponseAfter10Sec()).build();
    }

    public Response bulkheadFallback(String inputCode) {
        log.info("Falling back to ResilienceStrategiesResource#bulkheadFallback()");
        return Response.status(Response.Status.TOO_MANY_REQUESTS).build();
    }

    @GET
    @Path("/call-with-timeout")
    @Timeout(100) //need to complete in less than 100 ms or a TimeoutException will be thrown
    @Fallback(fallbackMethod = "timeoutFallback") //if any exception is thrown
    public Response getTimeout() throws InterruptedException {
        Thread.sleep(200);
        return Response.ok().build();
    }
    public Response timeoutFallback() {
        return Response.status(Response.Status.GATEWAY_TIMEOUT).build();
    }

    @GET
    @Path("/call-retry")
    @Timeout(100)
    @Retry(delay = 100, //delay between reties
            jitter = 25, //variance of time between reties (85 - 125)
            maxRetries = 3,
            retryOn = TimeoutException.class) //other exceptions will be handled normally
    @Fallback(fallbackMethod = "timeoutFallback")
    public Response getRetry() {
        //the service method will delay the return to trigger the timeout for 2 times only
        return Response.ok().entity(serviceForRetry.doesNotDelayAfterRepeatedCalls()).build();
    }

    @GET
    @Path("/call-circuit-breaker")
    @Bulkhead(1)
    @CircuitBreaker(
            requestVolumeThreshold=3,
            failureRatio=.66,
            delay = 5,
            delayUnit = ChronoUnit.SECONDS,
            successThreshold=2
    )
    @Fallback(value = TransactionServiceFallbackHandler.class) //handler class to support multiple type of exceptions
    public Response callCircuitBreakerService() {
        return Response.ok().entity(circuitBreakerBackendService.callService()).build();
    }


}
