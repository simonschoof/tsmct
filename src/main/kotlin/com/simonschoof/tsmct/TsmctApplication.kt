package com.simonschoof.tsmct

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class TsmctApplication

fun main(args: Array<String>) {
	EmbeddedPostgres.builder().setPort(5432).start()
	runApplication<TsmctApplication>(*args)
}
