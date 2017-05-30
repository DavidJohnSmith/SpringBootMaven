package hello.controller;

import hello.bean.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by dongxie on 2017/5/7.
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private static Logger log = LoggerFactory.getLogger(GreetingController.class);
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("this is run");
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }


}
