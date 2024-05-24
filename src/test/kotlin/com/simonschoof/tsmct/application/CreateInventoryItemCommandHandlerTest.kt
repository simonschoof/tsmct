package io.ko.com.simonschoof.tsmct.application

import com.simonschoof.tsmct.application.CreateInventoryItemCommandHandler
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.infrastructure.EventStoreAggregateRepository
import com.simonschoof.tsmct.infrastructure.EventTable
import com.simonschoof.tsmct.infrastructure.KediatorEventBus
import com.simonschoof.tsmct.infrastructure.KtormEventStore
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
                KediatorEventBus::class
            ]
        )
    ]
)
class CreateInventoryItemCommandHandlerTest(
    private val createInventoryItemCommandHandler: CreateInventoryItemCommandHandler,
    private val database: Database
) : DatabaseSpec({

    beforeTest() {
        database.deleteAll(EventTable)
    }

    test("initial commandhandler test") {
        // arrange
        val command = CreateInventoryItem("test")

        // act
        createInventoryItemCommandHandler.handle(command)

        // assert
        database.from(EventTable).select().totalRecordsInAllPages shouldBe 1
    }
})
