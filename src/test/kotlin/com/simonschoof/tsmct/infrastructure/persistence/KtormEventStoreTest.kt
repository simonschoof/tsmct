package com.simonschoof.tsmct.infrastructure.persistence

import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.Event
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
            val aggregateType = RepositoryTestAggregate::class.simpleName!!
            val baseEventInfo = RepositoryTestAggregate.invoke("test").baseEventInfo()
            val repositoryTestAggregateCreated = RepositoryTestAggregateCreated(baseEventInfo, "test")
            val repositoryTestAggregateChanged = RepositoryTestAggregateNameChanged(baseEventInfo, "newName")
            val events = listOf(repositoryTestAggregateCreated, repositoryTestAggregateChanged)

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
            val aggregateType = RepositoryTestAggregate::class.simpleName!!
            val baseEventInfo = RepositoryTestAggregate.invoke("test").baseEventInfo()
            val repositoryTestAggregateCreated = RepositoryTestAggregateCreated(baseEventInfo, "test")
            val repositoryTestAggregateChanged = RepositoryTestAggregateNameChanged(baseEventInfo, "newName")
            val events = listOf(repositoryTestAggregateCreated, repositoryTestAggregateChanged)
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
            is RepositoryTestAggregateCreated -> notification.name shouldBe "test"
            is RepositoryTestAggregateNameChanged -> notification.newName shouldBe "newName"
        }
    }
}