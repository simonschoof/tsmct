package com.simonschoof.cqrses.configs

import org.ktorm.database.Database
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class KtormConfig(private val dataSource: DataSource) {
    @Bean
    fun database(): Database {
        return Database.connectWithSpringSupport(dataSource, PostgreSqlDialect())
    }
}