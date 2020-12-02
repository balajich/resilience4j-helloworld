package org.eduami.spring;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@RestController
public class GreetingController {
    private String cache = null;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/greeting")
    @TimeLimiter(name = "timelimiterSlow", fallbackMethod = "greetingFallBack")
    @Bulkhead(name = "greetingBulkhead", fallbackMethod = "greetingFallBack", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        String result = restTemplate.getForEntity("http://localhost:8081/serviceBgreeting?name=" + name, String.class).getBody().toString();
        cache = result;//update cache
        return CompletableFuture.completedFuture(result);
    }


    //Invoked when call to serviceB timeout
    private CompletableFuture<String> greetingFallBack(String name, Exception ex) {
        System.out.println("Call to serviceB is timed out");
        //return data from cache
        return CompletableFuture.completedFuture(cache);
    }

    public ResponseEntity greetingFallBack(String name, io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        System.out.println("BulkHead applied no further calls are accepted");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "10"); //retry after 10 seconds

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(responseHeaders) //send retry header
                .body("Too many concurrent requests- Please try after some time");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
