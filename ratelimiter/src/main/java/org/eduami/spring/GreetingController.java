package org.eduami.spring;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    @RateLimiter(name = "greetingRateLimit", fallbackMethod = "greetingFallBack")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok().body("Hello World: " + name);
    }


    public ResponseEntity greetingFallBack(String name, io.github.resilience4j.ratelimiter.RequestNotPermitted ex) {
        System.out.println("Rate limit applied no further calls are accepted");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "1"); //retry after one second

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(responseHeaders) //send retry header
                .body("Too many request - No further calls are accepted");
    }
}
