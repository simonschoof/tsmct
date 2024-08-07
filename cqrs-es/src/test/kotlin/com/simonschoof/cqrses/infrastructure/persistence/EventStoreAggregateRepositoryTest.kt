package com.simonschoof.cqrses.infrastructure.persistence

import com.simonschoof.cqrses.DatabaseSpec
import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
import com.simonschoof.cqrses.infrastructure.AggregateQualifiedNameProvider
import com.simonschoof.cqrses.infrastructure.EventQualifiedNameProvider
import com.simonschoof.cqrses.infrastructure.SpringEventBus
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import java.util.Optional

@DataJpaTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                EventStoreAggregateRepository::class,
                KtormEventStore::class,
                SpringEventBus::class,
                EventQualifiedNameProvider::class,
                AggregateQualifiedNameProvider::class,
            ]
        )
    ]
)
class EventStoreAggregateRepositoryTest(
    private val repository: EventStoreAggregateRepository<RepositoryTestAggregate>,
    private val database: Database
) : DatabaseSpec({

    beforeTest {
        database.deleteAll(EventTable)
    }

    context("save") {
        test("should save events to the database if aggregate id is present") {
            // Arrange
            val aggregate = RepositoryTestAggregate.invoke("test")

            // Act
            repository.save(aggregate)

            // Assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe 1
        }

        test("should not save events to the database if aggregate id is not present") {
            // Arrange
            val aggregate = mockk<RepositoryTestAggregate>()
            every { aggregate.id } returns Optional.empty()

            // Act
            repository.save(aggregate)

            // Assert
            database.from(EventTable).select().totalRecordsInAllPages shouldBe 0
        }
    }

    context("getById") {
        test("should return an aggregate if events are found") {
            // Arrange
            val aggregate =
                RepositoryTestAggregate.invoke("test").changeName("newName")

            repository.save(aggregate)

            // Act
            val result = repository.getById(aggregate.id.get())

            // Assert
            result.isPresent shouldBe true
            result.get().id shouldBe aggregate.id
        }

        test("should return an empty Optional if no events are found") {
            // Arrange
            val aggregateId = AggregateId.randomUUID()

            // Act
            val result = repository.getById(aggregateId)

            // Assert
            result.isPresent shouldBe false
        }
    }
})



