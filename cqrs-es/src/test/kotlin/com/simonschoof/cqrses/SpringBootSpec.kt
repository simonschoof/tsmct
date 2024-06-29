package com.simonschoof.cqrses

import io.kotest.core.spec.style.FunSpec
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.test.annotation.FlywayTest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
abstract class SpringBootSpec(body: FunSpec.() -> Unit) : FunSpec(body)