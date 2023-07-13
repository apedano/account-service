package accountservice.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@ApplicationScoped
@Liveness
public class HealthConfigBasedLivenessCheck implements HealthCheck {

    @Inject
    private HealthConfig healthConfig;

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("HealthConfigBased")
                .withData("Config value", healthConfig.isHealty())
                .status(healthConfig.isHealty())
                .build();
    }
}
