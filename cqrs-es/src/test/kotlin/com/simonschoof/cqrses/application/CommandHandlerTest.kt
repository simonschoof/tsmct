package com.simonschoof.cqrses.application

import com.simonschoof.cqrses.DatabaseSpec
import com.simonschoof.cqrses.domain.ChangeInventoryItemName
import com.simonschoof.cqrses.domain.ChangeMaxQuantity
import com.simonschoof.cqrses.domain.CheckInInventoryItems
import com.simonschoof.cqrses.domain.CreateInventoryItem
import com.simonschoof.cqrses.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.cqrses.domain.InventoryItemNameChanged
import com.simonschoof.cqrses.domain.InventoryItemsCheckedIn
import com.simonschoof.cqrses.domain.InventoryItemsRemoved
import com.simonschoof.cqrses.domain.RemoveInventoryItems
import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
import com.simonschoof.cqrses.infrastructure.AggregateQualifiedNameProvider
import com.simonschoof.cqrses.infrastructure.EventQualifiedNameProvider
import com.simonschoof.cqrses.infrastructure.SpringEventBus
import com.simonschoof.cqrses.infrastructure.persistence.EventStoreAggregateRepository
import com.simonschoof.cqrses.infrastructure.persistence.EventTable
import com.simonschoof.cqrses.infrastructure.persistence.KtormEventStore
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@DataJpaTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                InventoryItemCommandHandlers::class,
                EventStoreAggregateRepository::class,
                KtormEventStore::class,
                SpringEventBus::class,
                EventQualifiedNameProvider::class,
                AggregateQualifiedNameProvider::class
            ]
        )
    ]
)
class CommandHandlerTest(
    private val inventoryItemCommandHandlers: InventoryItemCommandHandlers,
    private val database: Database
) : DatabaseSpec({

    lateinit var aggregateId: AggregateId

    beforeTest {
        database.deleteAll(EventTable)
        val command = CreateInventoryItem("test", 10, 15)
        inventoryItemCommandHandlers.handle(command)
        aggregateId = database.from(EventTable).select().map { it[EventTable.aggregateId]!! }.first()
    }

    context("createInventoryItemCommandHandler") {
        test("createInventoryItemCommandHandler test") {
            // assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe 1
        }
    }

    context("changeInventoryItemNameCommandHandler") {
        test("changeInventoryItemNameCommandHandler test") {
            // arrange
            val command = ChangeInventoryItemName(aggregateId, "newName")

            // act
            inventoryItemCommandHandlers.handle(command)

            // assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe 2
            database.from(EventTable).select().map { it[EventTable.eventType]!! }.last() shouldBe  InventoryItemNameChanged::class.simpleName
        }
    }

    context("checkInInventoryItemsCommandHandler") {
        test("checkInInventoryItemsCommandHandler test") {
            // arrange
            val command = CheckInInventoryItems(aggregateId, 5)

            // act
            inventoryItemCommandHandlers.handle(command)

            // assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe 2
            database.from(EventTable).select().map { it[EventTable.eventType]!! }.last() shouldBe  InventoryItemsCheckedIn::class.simpleName
        }
    }

    context("removeInventoryItemsCommandHandler") {
        test("removeInventoryItemsCommandHandler test") {
            // arrange
            val command = RemoveInventoryItems(aggregateId, 5)

            // act
            inventoryItemCommandHandlers.handle(command)

            // assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe 2
            database.from(EventTable).select().map { it[EventTable.eventType]!! }.last() shouldBe  InventoryItemsRemoved::class.simpleName
        }
    }

    context("changeMaxQuantityCommandHandler") {
        test("changeMaxQuantityCommandHandler test") {
            // arrange
            val command = ChangeMaxQuantity(aggregateId, 15)

            // act
            inventoryItemCommandHandlers.handle(command)

            // assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe 2
            database.from(EventTable).select().map { it[EventTable.eventType]!! }.last() shouldBe  InventoryItemMaxQuantityChanged::class.simpleName
        }
    }

})
