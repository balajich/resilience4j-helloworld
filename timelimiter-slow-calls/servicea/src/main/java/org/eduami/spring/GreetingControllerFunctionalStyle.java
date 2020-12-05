package org.eduami.spring;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RestController
public class GreetingControllerFunctionalStyle {
    TimeLimiterConfig config = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(1))
            .cancelRunningFuture(true)
            .build();
    private String cache = null;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/greetingFunctional")
    public ResponseEntity<String> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        String result = null;
        try {
            TimeLimiter timeLimiter = TimeLimiter.of(config);
            result = timeLimiter.executeFutureSupplier(
                    () -> CompletableFuture.supplyAsync(() -> restTemplate.getForEntity("http://localhost:8081/serviceBgreeting?name=" + name, String.class).getBody()));
            cache = result;
        } catch (Exception e) {
            // request time out
            System.out.println("Time out exception : "+ e);
            result = cache;

        }
        return ResponseEntity.ok().body(result);
    }


}
