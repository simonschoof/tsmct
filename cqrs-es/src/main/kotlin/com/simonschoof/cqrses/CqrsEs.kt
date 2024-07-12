package com.simonschoof.cqrses

import io.github.oshai.kotlinlogging.KotlinLogging
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

private val logger = KotlinLogging.logger {}

@SpringBootApplication
@EnableTransactionManagement
class CqrsES

fun main(args: Array<String>) {
	logger.info { "Starting CqrsES" }
	logger.info { "args: ${args.joinToString()}" }
	when (args.contains("--use-external-postgres")) {
		true -> logger.info { "Using external Postgres" }
		false -> {
			logger.info { "Starting embedded Postgres" }
			EmbeddedPostgres.builder().setPort(5432).start()
		}
	}
	runApplication<CqrsES>(*args)
}
