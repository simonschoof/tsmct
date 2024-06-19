package com.simonschoof.tsmct

import com.simonschoof.tsmct.application.InventoryItemCommandHandlers
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.infrastructure.persistence.EventTable
import io.ko.com.simonschoof.tsmct.SpringBootSpec
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.select

class TsmctApplicationTests(
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
