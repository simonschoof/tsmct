package io.ko.com.simonschoof.tsmct.application

import com.simonschoof.tsmct.application.InventoryItemCommandHandlers
import com.simonschoof.tsmct.domain.ChangeInventoryItemName
import com.simonschoof.tsmct.domain.ChangeMaxQuantity
import com.simonschoof.tsmct.domain.CheckInInventoryItems
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.tsmct.domain.InventoryItemNameChanged
import com.simonschoof.tsmct.domain.InventoryItemsCheckedIn
import com.simonschoof.tsmct.domain.InventoryItemsRemoved
import com.simonschoof.tsmct.domain.RemoveInventoryItems
import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.infrastructure.AggregateQualifiedNameProvider
import com.simonschoof.tsmct.infrastructure.EventQualifiedNameProvider
import com.simonschoof.tsmct.infrastructure.SpringEventBus
import com.simonschoof.tsmct.infrastructure.persistence.EventStoreAggregateRepository
import com.simonschoof.tsmct.infrastructure.persistence.EventTable
import com.simonschoof.tsmct.infrastructure.persistence.KtormEventStore
import io.ko.com.simonschoof.tsmct.DatabaseSpec
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