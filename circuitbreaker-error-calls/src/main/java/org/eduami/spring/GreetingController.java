package org.eduami.spring;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    @CircuitBreaker(name = "greetingCircuit", fallbackMethod = "greetingFallBack")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
    }

    public ResponseEntity greetingFallBack(String name, io.github.resilience4j.circuitbreaker.CallNotPermittedException ex) {
        System.out.println("Circuit is in open state no further calls are accepted");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "5"); //retry after 5 seconds

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(responseHeaders) //send retry header
                .body("No further calls are accepted,Currently Service is experiencing internal errors - Please try after 5 seconds");
    }

    public ResponseEntity greetingFallBack(String name, HttpServerErrorException ex) {
        System.out.println("Exception occurred  during the call");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Sorry: HttpServerErrorException");
    }


}
