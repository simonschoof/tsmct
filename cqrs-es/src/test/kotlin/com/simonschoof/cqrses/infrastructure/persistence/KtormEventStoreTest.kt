package com.simonschoof.cqrses.infrastructure.persistence

import com.ninjasquad.springmockk.SpykBean
import com.simonschoof.cqrses.DatabaseSpec
import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
import com.simonschoof.cqrses.domain.buildingblocks.BaseEventInfo
import com.simonschoof.cqrses.domain.buildingblocks.Event
import com.simonschoof.cqrses.infrastructure.EventQualifiedNameProvider
import com.simonschoof.cqrses.infrastructure.SpringEventBus
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.verify
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.Instant

private val logger = KotlinLogging.logger {}

@DataJpaTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                KtormEventStore::class,
                SpringEventBus::class,
                EventQualifiedNameProvider::class,
                KtormEventStoreTestEventHandler::class
            ]
        )
    ]
)
class KtormEventStoreTest(
    private val eventStore: KtormEventStore,
    private val database: Database,
    @SpykBean val eventHandler: KtormEventStoreTestEventHandler
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
            eventStore.saveEvents(aggregateId, aggregateType, events)

            // Assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe events.size

            verify(exactly = 2) { eventHandler.handle(any()) }

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
            eventStore.saveEvents(aggregateId, aggregateType, events)


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

    test("Events should be returned in ascending order by timestamp") {
        // Arrange
        val aggregateId = AggregateId.randomUUID()
        val aggregateType = "TestAggregate"
        val now = Instant.now()
        val baseEventInfo = BaseEventInfo(aggregateId, aggregateType, now.minusSeconds(60))
        val baseEventInfo2 = BaseEventInfo(aggregateId, aggregateType, now.minusSeconds(30))
        val baseEventInfo3 = BaseEventInfo(aggregateId, aggregateType, now)
        val event1 = RepositoryTestAggregateCreated(baseEventInfo, "First Event")
        val event2 = RepositoryTestAggregateNameChanged(baseEventInfo2, "Second Event")
        val event3 = RepositoryTestAggregateNameChanged(baseEventInfo3, "Third Event")
        eventStore.saveEvents(aggregateId, aggregateType, listOf(event1, event2, event3))

        // Act
        val result = eventStore.getEventsForAggregate(aggregateId)

        // Assert
        result.size shouldBe 3
        result[0].shouldBeTypeOf<RepositoryTestAggregateCreated>()
        (result[0] as RepositoryTestAggregateCreated).name shouldBe "First Event"
        result[1].shouldBeTypeOf<RepositoryTestAggregateNameChanged>()
        (result[1] as RepositoryTestAggregateNameChanged).newName shouldBe "Second Event"
        result[2].shouldBeTypeOf<RepositoryTestAggregateNameChanged>()
        (result[2] as RepositoryTestAggregateNameChanged).newName shouldBe "Third Event"
    }
})

@Component
class KtormEventStoreTestEventHandler {
    @EventListener
    fun handle(notification: Event) {
        logger.info { "Event handled: $notification" }
    }
}