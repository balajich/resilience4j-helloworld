package org.eduami.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class GreetingController {
    Random random = new Random(-6732303926L);
    @GetMapping("/serviceBgreeting")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "serviceB") String name) {
        return generateSlowBehavior(name);
    }

    private ResponseEntity generateSlowBehavior(String name) {
        int i = random.nextInt(2);
        if (i == 0) {
            try {
                Thread.sleep(2*1000);//sleep for two seconds
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }//end of if
        return ResponseEntity.ok().body("Hello " + name);
    }


}
