package com.simonschoof.cqrses.readmodels

import com.simonschoof.cqrses.domain.InventoryItemCreated
import com.simonschoof.cqrses.domain.InventoryItemNameChanged
import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
import com.simonschoof.cqrses.domain.buildingblocks.BaseEventInfo
import com.simonschoof.cqrses.readmodels.InventoryItemDetailView
import com.simonschoof.cqrses.readmodels.InventoryItemListView
import com.simonschoof.cqrses.readmodels.KtormReadModelFacade
import com.simonschoof.cqrses.readmodels.ReadModelInventoryItemDetailsTable
import com.simonschoof.cqrses.readmodels.ReadModelInventoryItemTable
import com.simonschoof.cqrses.DatabaseSpec
import io.kotest.matchers.shouldBe
import org.ktorm.database.Database
import org.ktorm.dsl.deleteAll
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import java.time.Clock

@DataJpaTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                KtormReadModelFacade::class,
                InventoryItemListView::class,
                InventoryItemDetailView::class
            ]
        )
    ]
)
class KtormReadModelFacadeTest(
    private val database: Database,
    private val clock: Clock,
    private val readModelFacade: KtormReadModelFacade,
    private val listView: InventoryItemListView,
    private val detailView: InventoryItemDetailView,
    private val rmiit: ReadModelInventoryItemTable = ReadModelInventoryItemTable.aliased("rmiit"),
    private val rmiidt: ReadModelInventoryItemDetailsTable = ReadModelInventoryItemDetailsTable.aliased("rmiidt")
) : DatabaseSpec({

    beforeTest {
        database.deleteAll(rmiit)
        database.deleteAll(rmiit)
    }

    test("getInventoryItems and getInventoryItemDetails") {
        // Arrange
        val baseEventInfo1 = BaseEventInfo(AggregateId.randomUUID(), "InventoryItem", clock.instant())
        val baseEventInfo2 = BaseEventInfo(AggregateId.randomUUID(), "InventoryItem", clock.instant())
        val event = InventoryItemCreated(baseEventInfo1, "test", 10, 15)
        val event2 = InventoryItemCreated(baseEventInfo2, "test2", 20, 25)
        val event3 = InventoryItemNameChanged(baseEventInfo2, "newName")

        listView.handle(event)
        listView.handle(event2)
        listView.handle(event3)
        detailView.handle(event)
        detailView.handle(event2)
        detailView.handle(event3)

        // Act
        val inventoryItems = readModelFacade.getInventoryItems()
        val inventoryItemDetails1 = readModelFacade.getInventoryItemDetails(baseEventInfo1.aggregateId)
        val inventoryItemDetails2 = readModelFacade.getInventoryItemDetails(baseEventInfo2.aggregateId)

        // Assert
        inventoryItems.size shouldBe 2
        inventoryItemDetails1.isPresent shouldBe true
        inventoryItemDetails2.isPresent shouldBe true

    }
})
