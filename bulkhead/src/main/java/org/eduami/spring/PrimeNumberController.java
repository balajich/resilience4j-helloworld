package org.eduami.spring;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class PrimeNumberController {

    @GetMapping("/getPrimeNumbers")
    @Bulkhead(name = "greetingBulkhead", fallbackMethod = "getPrimeNumbersFallBack")
    public ResponseEntity getPrimeNumbers(@RequestParam(value = "number", defaultValue = "1234567") Integer number) {
        return ResponseEntity.ok().body(getPrimeNumbersBruteForce(number));
    }


    public ResponseEntity getPrimeNumbersFallBack(Integer number, io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        System.out.println("BulkHead applied no further calls are accepted");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "10"); //retry after 10 seconds

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(responseHeaders) //send retry header
                .body("Too many concurrent requests- Please try after some time");
    }

    /**
     * This function takes really long time for larger numbers
     * It has Horrible time complexity of O(n!)
     * @param n
     * @return
     */
    public static List<Integer> getPrimeNumbersBruteForce(int n) {
        List<Integer> primeNumbers = new LinkedList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrimeBruteForce(i)) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }
    public static boolean isPrimeBruteForce(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
