package com.simonschoof.tsmct

import com.simonschoof.tsmct.application.CreateInventoryItemCommandHandler
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.infrastructure.EventTable
import io.ko.com.simonschoof.tsmct.SpringBootSpec
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.select

class TsmctApplicationTests(
	private val createInventoryItemCommandHandler: CreateInventoryItemCommandHandler,
	private val database: Database): SpringBootSpec({

	beforeTest() {
		database.deleteAll(EventTable)
	}

	test("load the spring context") { }


	test("initial commandhandler test") {
		// arrange
		val command = CreateInventoryItem("test")

		// act
		createInventoryItemCommandHandler.handle(command)

		// assert
		database.from(EventTable).select().totalRecordsInAllPages shouldBe 1
	}
})
