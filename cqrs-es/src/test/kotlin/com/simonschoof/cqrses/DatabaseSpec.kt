package com.simonschoof.cqrses

import com.simonschoof.cqrses.configs.DomainClassNamesConfig
import com.simonschoof.cqrses.configs.JacksonConfig
import com.simonschoof.cqrses.configs.KtormConfig
import io.kotest.core.spec.style.FunSpec
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import org.flywaydb.test.annotation.FlywayTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@DataJpaTest
@FlywayTest
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
@ActiveProfiles("test")
@ContextConfiguration(
    classes = [
        CqrsES::class,
        KtormConfig::class,
        FixedClockConfig::class,
        JacksonConfig::class,
        DomainClassNamesConfig::class
    ]
)
abstract class DatabaseSpec(body: FunSpec.() -> Unit) : FunSpec(body)

internal class FixedClockConfig {
    @Bean
    fun fixedClock(): Clock = Clock.fixed(Instant.now(), ZoneId.of("UTC"))
}

