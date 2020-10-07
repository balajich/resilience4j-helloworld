package org.eduami.spring;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    @Bulkhead(name = "greetingBulkhead", fallbackMethod = "greetingFallBack")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok().body("Hello World: " + name);
    }


    public ResponseEntity greetingFallBack(String name, io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        System.out.println("BulkHead applied no further calls are accepted");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "10"); //retry after 10 seconds

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(responseHeaders) //send retry header
                .body("Too many concurrent requests- Please try after some time");
    }
}
