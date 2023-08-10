package com.simonschoof.tsmct

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TsmctApplication

fun main(args: Array<String>) {
	runApplication<TsmctApplication>(*args)
}
