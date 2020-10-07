package org.eduami.spring;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    private ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
  
    @GetMapping("/greeting")

    @Retry(name = "greetingRetry")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        // This method returns Hello World only when called three times
        Integer calledCount = count.get();
        System.out.println(String.format("Called count %d", calledCount));
        if (calledCount < 2) {
            count.set(++calledCount);
            throw new RuntimeException("Unable to serve request");
        }
        return ResponseEntity.ok().body("Hello World: " + name);
    }

}
