package com.simonschoof.tsmct

import io.github.oshai.kotlinlogging.KotlinLogging
import org.ktorm.database.Database
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

private val logger = KotlinLogging.logger {}
data class Greeting(val id: Long, val content: String)

@RestController
class HelloWorldController(private val database: Database?) {
    private val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String?): Greeting {
        logger.info { "Test: ${database?.name}" }
        return Greeting(counter.incrementAndGet(), String.format(template, name))
    }

    @GetMapping("/add")
    fun add(@RequestParam(value = "x", defaultValue = "1") x: String?, @RequestParam(value = "y", defaultValue = "1") y: String?): Int {
        return (x?.toInt() ?: 0) + (y?.toInt() ?: 0)
    }

    companion object {
        private const val template = "Hello, %s!"
    }
}