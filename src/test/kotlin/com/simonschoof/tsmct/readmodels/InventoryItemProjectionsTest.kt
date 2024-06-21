package io.ko.com.simonschoof.tsmct.readmodels

import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.simonschoof.tsmct.domain.InventoryItemDeactivated
import com.simonschoof.tsmct.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.tsmct.domain.InventoryItemNameChanged
import com.simonschoof.tsmct.domain.InventoryItemsCheckedIn
import com.simonschoof.tsmct.domain.InventoryItemsRemoved
import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.BaseEventInfo
import com.simonschoof.tsmct.readmodels.InventoryItemDetailView
import com.simonschoof.tsmct.readmodels.InventoryItemListView
import com.simonschoof.tsmct.readmodels.ReadModelInventoryItemDetailsTable
import com.simonschoof.tsmct.readmodels.ReadModelInventoryItemTable
import io.ko.com.simonschoof.tsmct.DatabaseSpec
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import java.time.Clock

@DataJpaTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                InventoryItemListView::class,
                InventoryItemDetailView::class
            ]
        )
    ]
)
class InventoryItemProjectionsTest(
    private val database: Database,
    private val clock: Clock,
    private val listView: InventoryItemListView,
    private val detailView: InventoryItemDetailView,
    private val rmiit: ReadModelInventoryItemTable = ReadModelInventoryItemTable.aliased("rmiit"),
    private val rmiidt: ReadModelInventoryItemDetailsTable = ReadModelInventoryItemDetailsTable.aliased("rmiidt")
) : DatabaseSpec({

    lateinit var baseEventInfo: BaseEventInfo
    beforeTest {
        database.deleteAll(ReadModelInventoryItemTable)
        database.deleteAll(ReadModelInventoryItemDetailsTable)
        baseEventInfo = BaseEventInfo(AggregateId.randomUUID(), "InventoryItem", clock.instant())
    }

    context("InventoryItemListView") {
        test("should add a new inventory item when an InventoryItemCreated event is handled") {
            // Arrange
            val event = InventoryItemCreated(baseEventInfo, "test", 10, 15)

            // Act
            listView.handle(event)

            // Assert
            val result = database.from(rmiit).select().totalRecordsInAllPages
            result shouldBe 1
        }

        test("should update the name of an inventory item when an InventoryItemNameChanged event is handled") {
            // Arrange
            listView.handle(InventoryItemCreated(baseEventInfo, "test", 10, 15))
            val event = InventoryItemNameChanged(baseEventInfo, "newName")

            // Act
            listView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiit)
                .find { it.aggregateId eq event.aggregateId }?.name
            result shouldBe event.newName
        }

        test("should remove an inventory item when an InventoryItemDeactivated event is handled") {
            // Arrange
            listView.handle(InventoryItemCreated(baseEventInfo, "test", 10, 15))
            val event = InventoryItemDeactivated(baseEventInfo)

            // Act
            listView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiit).find { it.aggregateId eq event.aggregateId }
            result shouldBe null
        }
    }

    context("InventoryItemDetailView") {
        test("should add a new inventory item when an InventoryItemCreated event is handled") {
            // Arrange
            val event = InventoryItemCreated(baseEventInfo, "test", 10, 15)

            // Act
            detailView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiidt).find { it.aggregateId eq event.aggregateId }
            result?.name shouldBe event.name
            result?.maxQuantity shouldBe event.maxQuantity
            result?.availableQuantity shouldBe event.availableQuantity
        }

        test("should update the name of an inventory item when an InventoryItemNameChanged event is handled") {
            // Arrange
            detailView.handle(InventoryItemCreated(baseEventInfo, "test", 10, 15))
            val event = InventoryItemNameChanged(baseEventInfo, "newName")

            // Act
            detailView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiidt)
                .find { it.aggregateId eq event.aggregateId }?.name
            result shouldBe event.newName
        }

        test("should remove an inventory item when an InventoryItemDeactivated event is handled") {
            // Arrange
            detailView.handle(InventoryItemCreated(baseEventInfo, "test", 10, 15))
            val event = InventoryItemDeactivated(baseEventInfo)

            // Act
            detailView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiidt).find { it.aggregateId eq event.aggregateId }
            result shouldBe null
        }

        test("should update the available quantity of an inventory item when an InventoryItemsCheckedIn event is handled") {
            // Arrange
            detailView.handle(InventoryItemCreated(baseEventInfo, "test", 10, 15))
            val event = InventoryItemsCheckedIn(baseEventInfo, 5, 15)

            // Act
            detailView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiidt)
                .find { it.aggregateId eq event.aggregateId }?.availableQuantity
            result shouldBe 15
        }

        test("should update the available quantity of an inventory item when an InventoryItemsRemoved event is handled") {
            // Arrange
            detailView.handle(InventoryItemCreated(baseEventInfo, "test", 10, 15))
            val event = InventoryItemsRemoved(baseEventInfo, 5, 5)

            // Act
            detailView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiidt)
                .find { it.aggregateId eq event.aggregateId }?.availableQuantity
            result shouldBe 5
        }

        test("should update the max quantity of an inventory item when an InventoryItemMaxQuantityChanged event is handled") {
            // Arrange
            detailView.handle(InventoryItemCreated(baseEventInfo, "test", 10, 15))
            val event = InventoryItemMaxQuantityChanged(baseEventInfo, 20)

            // Act
            detailView.handle(event)

            // Assert
            val result = database.sequenceOf(rmiidt)
                .find { it.aggregateId eq event.aggregateId }?.maxQuantity
            result shouldBe 20
        }
    }

})
