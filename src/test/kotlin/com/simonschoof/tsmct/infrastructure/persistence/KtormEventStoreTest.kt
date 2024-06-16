package com.simonschoof.tsmct.infrastructure.persistence

import com.simonschoof.tsmct.domain.InventoryItem
import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.simonschoof.tsmct.domain.InventoryItemNameChanged
import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.Event
import com.simonschoof.tsmct.infrastructure.AggregateQualifiedNameProvider
import com.simonschoof.tsmct.infrastructure.EventQualifiedNameProvider
import com.simonschoof.tsmct.infrastructure.KediatorEventBus
import com.trendyol.kediatr.NotificationHandler
import io.ko.com.simonschoof.tsmct.DatabaseSpec
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.stereotype.Component

@DataJpaTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                KtormEventStore::class,
                KediatorEventBus::class,
                EventQualifiedNameProvider::class,
                AggregateQualifiedNameProvider::class,
                KtormEventStoreTestEventHandler::class
            ]
        )
    ]
)
class KtormEventStoreTest(
    private val eventStore: KtormEventStore,
    private val database: Database
) : DatabaseSpec({



    beforeTest {
        database.deleteAll(EventTable)
    }

    context("saveEvents") {
        test("saveEvents should save events to the database and publish them to the event bus") {
            // Arrange
            val aggregateId = AggregateId.randomUUID()
            val aggregateType = InventoryItem::class.simpleName!!
            val baseEventInfo = InventoryItem.invoke("test", 5, 10).baseEventInfo()
            val inventoryItemCreated = InventoryItemCreated(baseEventInfo, "test", 5, 10)
            val inventoryItemNameChanged = InventoryItemNameChanged(baseEventInfo, "newName")
            val events = listOf(inventoryItemCreated, inventoryItemNameChanged)

            // Act
            runBlocking { eventStore.saveEvents(aggregateId, aggregateType, events) }

            // Assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe events.size
            // The event handler should have been called twice.
            // See KtormEventStoreTestEventHandler class below for the handler implementation

        }
    }

    context("getEventsForAggregate") {
        test("getEventsForAggregate should return events for the given aggregate ID") {
            // Arrange
            val aggregateId = AggregateId.randomUUID()
            val aggregateType = InventoryItem::class.simpleName!!
            val baseEventInfo = InventoryItem.invoke("test", 5, 10).baseEventInfo()
            val inventoryItemCreated = InventoryItemCreated(baseEventInfo, "test", 5, 10)
            val inventoryItemNameChanged = InventoryItemNameChanged(baseEventInfo, "newName")
            val events = listOf(inventoryItemCreated, inventoryItemNameChanged)
            runBlocking { eventStore.saveEvents(aggregateId, aggregateType, events) }


            // Act
            val result = eventStore.getEventsForAggregate(aggregateId)

            // Assert
            result.size shouldBe events.size
            result.shouldContainAll(events)
        }

        test("getEventsForAggregate should return an empty list if no events are found") {
            // Arrange
            val aggregateId = AggregateId.randomUUID()

            // Act
            val result = eventStore.getEventsForAggregate(aggregateId)

            // Assert
            result.size shouldBe 0
        }
    }
})

@Component
class KtormEventStoreTestEventHandler : NotificationHandler<Event> {
    override suspend fun handle(notification: Event) {
        when (notification) {
            is InventoryItemCreated -> notification.name shouldBe "test"
            is InventoryItemNameChanged -> notification.newName shouldBe "newName"
        }
    }
}