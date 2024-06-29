package com.simonschoof.cqrses

import com.simonschoof.cqrses.application.InventoryItemCommandHandlers
import com.simonschoof.cqrses.domain.CreateInventoryItem
import com.simonschoof.cqrses.infrastructure.persistence.EventTable
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.select

class CqrsEsApplicationTests(
	private val inventoryItemCommandHandlers: InventoryItemCommandHandlers,
	private val database: Database): SpringBootSpec({

	beforeTest() {
		database.deleteAll(EventTable)
	}

	test("load the spring context") { }


	test("initial commandhandler test") {
		// arrange
		val command = CreateInventoryItem("test", 5, 10)

		// act
		inventoryItemCommandHandlers.handle(command)

		// assert
		database.from(EventTable).select().totalRecordsInAllPages shouldBe 1
	}
})
