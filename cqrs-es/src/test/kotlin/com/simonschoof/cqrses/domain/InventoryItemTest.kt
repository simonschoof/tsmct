package com.simonschoof.cqrses.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Optional

class InventoryItemTest : FunSpec({
    val initialName = "Initial Name"
    val initialAvailableQuantity = 10
    val initialMaxQuantity = 50
    val clock = Clock.fixed(
        Instant.now(), ZoneId.of(
            "UTC"
        )
    )

    lateinit var sut: InventoryItem

    beforeTest {
        sut = InventoryItem.invoke(
            inventoryItemName = initialName,
            availableQuantity = initialAvailableQuantity,
            maxQuantity = initialMaxQuantity,
            clock = clock
        )
    }

    test("InventoryItem should be created with the correct values") {
        // Assert
        sut.id.isPresent shouldBe true
        sut.changes.shouldNotBeEmpty()
        sut.changes.first().shouldBeTypeOf<InventoryItemCreated>()
        sut.changes.first() as InventoryItemCreated shouldBe InventoryItemCreated(
            sut.baseEventInfo(),
            initialName,
            initialAvailableQuantity,
            initialMaxQuantity
        )
    }

    test("changeName should update the name of the InventoryItem") {
        // Arrange
        val expected = "New Name"

        // Act
        val updatedInventoryItem = sut.changeName(expected)

        // Assert
        updatedInventoryItem.changes.shouldNotBeEmpty()
        updatedInventoryItem.changes.last().shouldBeTypeOf<InventoryItemNameChanged>()
        updatedInventoryItem.changes.last() as InventoryItemNameChanged shouldBe InventoryItemNameChanged(
            sut.baseEventInfo(),
            expected
        )
    }

    context("remove items") {

        test("remove should decrease the available quantity of the InventoryItem") {
            // Arrange

            val removeCount = 5
            val expected = initialAvailableQuantity - removeCount

            // Act
            val updatedInventoryItem = sut.remove(removeCount)

            // Assert
            updatedInventoryItem.changes.shouldNotBeEmpty()
            val inventoryItemsRemoved = updatedInventoryItem.changes.filterIsInstance<InventoryItemsRemoved>()
            inventoryItemsRemoved.count() shouldBe 1
            inventoryItemsRemoved.first().newAvailableQuantity shouldBe expected
        }

        test("remove should not add an event when trying to remove more items than available") {
            // Arrange

            val removeCount = 15 // More than the available quantity
            val expected = initialAvailableQuantity - removeCount
            // Act
            val updatedInventoryItem = sut.remove(removeCount)

            // Assert
            updatedInventoryItem.changes.filterIsInstance<InventoryItemsRemoved>().count() shouldBe 0
        }
    }

    context("check in items") {

        test("checkIn should increase the available quantity of the InventoryItem") {
            // Arrange
            val checkInCount = 5
            val expected = initialAvailableQuantity + checkInCount

            // Act
            val updatedInventoryItem = sut.checkIn(checkInCount)

            // Assert
            updatedInventoryItem.changes.shouldNotBeEmpty()
            val inventoryItemsCheckedIn = updatedInventoryItem.changes.filterIsInstance<InventoryItemsCheckedIn>()
            inventoryItemsCheckedIn.count() shouldBe 1
            inventoryItemsCheckedIn.first().newAvailableQuantity shouldBe expected
        }

        test("checkIn should not add an event when trying to check in more items than the max quantity allows") {
            // Arrange
            val checkInCount = Int.MAX_VALUE // More than the max quantity

            // Act
            val updatedInventoryItem = sut.checkIn(checkInCount)

            // Assert
            updatedInventoryItem.changes.filterIsInstance<InventoryItemsCheckedIn>().count() shouldBe 1
        }
    }

    test("changeMaxQuantity should update the max quantity of the InventoryItem") {
        // Arrange
        val expected = 100

        // Act
        val updatedInventoryItem = sut.changeMaxQuantity(expected)

        // Assert
        updatedInventoryItem.changes.shouldNotBeEmpty()
        val inventoryItemMaxQuantityChanged =
            updatedInventoryItem.changes.filterIsInstance<InventoryItemMaxQuantityChanged>()
        inventoryItemMaxQuantityChanged.count() shouldBe 1
        inventoryItemMaxQuantityChanged.first().newMaxQuantity shouldBe expected
    }

    test("deactivate should set isActivated to false") {
        // Arrange
        val expected = false

        // Act
        val updatedInventoryItem = sut.deactivate()

        // Assert
        updatedInventoryItem.changes.shouldNotBeEmpty()
        val inventoryItemDeactivated = updatedInventoryItem.changes.filterIsInstance<InventoryItemDeactivated>()
        inventoryItemDeactivated.count() shouldBe 1
    }

    context("load from history "){
        test("should apply all events and return the current state of the object") {
            val inventoryItem = InventoryItem()
            val baseEventInfo = inventoryItem.baseEventInfo(isNew = true)
            val event1 = InventoryItemCreated(
                baseEventInfo,
                "Test Item",
                100,
                500
            )
            val event2 = InventoryItemsRemoved(
                baseEventInfo,
                10,
                90
            )
            val event3 = InventoryItemsCheckedIn(
                baseEventInfo,
                5,
                95
            )

            val history = listOf(event1, event2, event3)

            val result = inventoryItem.loadFromHistory(history)

            result shouldBe InventoryItem(
                id = Optional.of(baseEventInfo.aggregateId),
                name = Optional.of("Test Item"),
                availableQuantity = 95,
                maxQuantity = 500,
                isActivated = true
            )

        }
    }
})
