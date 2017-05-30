package hellokotlin

import hello.Application
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by dongxie on 2017/5/7.
 */
@SpringBootApplication
open class Application {
    init {

    }
}
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}