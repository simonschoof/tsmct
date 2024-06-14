package io.ko.com.simonschoof.tsmct.application

import com.simonschoof.tsmct.application.CreateInventoryItemCommandHandler
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.infrastructure.AggregateQualifiedNameProvider
import com.simonschoof.tsmct.infrastructure.EventQualifiedNameProvider
import com.simonschoof.tsmct.infrastructure.KediatorEventBus
import com.simonschoof.tsmct.infrastructure.persistence.EventStoreAggregateRepository
import com.simonschoof.tsmct.infrastructure.persistence.EventTable
import com.simonschoof.tsmct.infrastructure.persistence.KtormEventStore
import io.ko.com.simonschoof.tsmct.DatabaseSpec
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@DataJpaTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                CreateInventoryItemCommandHandler::class,
                EventStoreAggregateRepository::class,
                KtormEventStore::class,
                KediatorEventBus::class,
                EventQualifiedNameProvider::class,
                AggregateQualifiedNameProvider::class
            ]
        )
    ]
)
class CreateInventoryItemCommandHandlerTest(
    private val createInventoryItemCommandHandler: CreateInventoryItemCommandHandler,
    private val database: Database
) : DatabaseSpec({

    beforeTest {
        database.deleteAll(EventTable)
    }

    test("initial command handler test") {
        // arrange
        val command = CreateInventoryItem("test", 5, 10)

        // act
        createInventoryItemCommandHandler.handle(command)

        // assert
        database.from(EventTable).select().totalRecordsInAllPages shouldBe 1
    }
})
