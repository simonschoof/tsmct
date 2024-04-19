package com.simonschoof.tsmct.configs

import org.ktorm.database.Database
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class KtormConfig(private val dataSource: DataSource) {
    @Bean
    fun database(): Database {
//        val pg = EmbeddedPostgres.builder().setPort(5432).start()
//        val embeddedDataSource = pg.postgresDatabase
//        val db = Database.connectWithSpringSupport(embeddedDataSource, PostgreSqlDialect())
//        return db
        return Database.connectWithSpringSupport(dataSource, PostgreSqlDialect())
    }
}