package accountservice.resources;

import accountservice.service.VerySlowServiceCaller;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.BulkheadException;

@Path("resilience")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ResilienceStrategiesResource {

    @Inject
    private VerySlowServiceCaller verySlowServiceCaller;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimeout() throws InterruptedException {
        Thread.sleep(200);
        return Response.ok().build();
    }
    public Response timeoutFallback() {
        return Response.status(Response.Status.GATEWAY_TIMEOUT).build();
    }


}
