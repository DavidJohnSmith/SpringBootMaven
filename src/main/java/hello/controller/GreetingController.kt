package hello.controller

import hello.bean.Greeting
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by dongxie on 2017/5/7.
 */
@RestController
class GreetingController {
    private val counter = AtomicLong()

    @RequestMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
        log.info("this is run")
        return Greeting(counter.incrementAndGet(),
                String.format(template, name))
    }

    companion object {

        private val template = "Hello, %s!"
        private val log = LoggerFactory.getLogger(GreetingController::class.java)
    }


}
