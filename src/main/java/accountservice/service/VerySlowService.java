package accountservice.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class VerySlowService {


    public String responseAfterWait() {
        String threadName = Thread.currentThread().getName();
        log.info("responseAfter10Sec called with thread: {}", threadName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("responseAfter10Sec finished with thread: {}", threadName);
        return "response";
    }


}
