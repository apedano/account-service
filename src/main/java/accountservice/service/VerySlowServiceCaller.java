package accountservice.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Bulkhead;

@ApplicationScoped
public class VerySlowServiceCaller {

    public static final Integer BULKHEAD_VALUE = 2;

    @Inject
    private VerySlowService verySlowService;

    public String doCallResponseAfter10Sec() {
        return verySlowService.responseAfterWait();
    }

}
