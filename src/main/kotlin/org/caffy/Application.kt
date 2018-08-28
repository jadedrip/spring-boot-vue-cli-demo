package org.caffy

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ApplicationConfig {
}

fun main(vararg args: String) {
    SpringApplication.run(ApplicationConfig::class.java, *args)
}
