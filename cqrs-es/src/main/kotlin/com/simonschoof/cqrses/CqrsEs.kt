package com.simonschoof.cqrses

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class CqrsES

fun main(args: Array<String>) {
	EmbeddedPostgres.builder().setPort(5432).start()
	runApplication<com.simonschoof.cqrses.CqrsES>(*args)
}
