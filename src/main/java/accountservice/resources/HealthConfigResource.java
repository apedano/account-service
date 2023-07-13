package accountservice.resources;


import accountservice.health.HealthConfig;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/health-config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HealthConfigResource {

    @Inject
    private HealthConfig healthConfig;

    @GET
    public HealthConfig getHealthConfig() {
        return this.healthConfig;
    }

    @POST
    public HealthConfig setHealthConfig(HealthConfig newHealthConfig) {
        this.healthConfig.setHealty(newHealthConfig.isHealty());
        this.healthConfig.setReady(newHealthConfig.isReady());
        return this.healthConfig;
    }


}
