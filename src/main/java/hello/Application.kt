package hello

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by dongxie on 2017/5/7.
 */
@SpringBootApplication
open class Application1 {
    init {

    }
}
fun main(args: Array<String>) {
    SpringApplication.run(Application1::class.java, *args)
}