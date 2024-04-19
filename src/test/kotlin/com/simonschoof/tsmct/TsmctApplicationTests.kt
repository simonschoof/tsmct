package com.simonschoof.tsmct

import io.kotest.core.spec.style.FunSpec
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.test.annotation.FlywayTest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
class TsmctApplicationTests: FunSpec({
		test("load the spring context") { }
})
