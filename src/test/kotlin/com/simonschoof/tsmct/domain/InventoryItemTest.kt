package io.ko.com.simonschoof.tsmct.domain

import com.simonschoof.tsmct.domain.InventoryItem
import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.simonschoof.tsmct.domain.InventoryItemDeactivated
import com.simonschoof.tsmct.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.tsmct.domain.InventoryItemsCheckedIn
import com.simonschoof.tsmct.domain.InventoryItemsRemoved
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

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

    test("changeName should update the name of the InventoryItem") {
        // Arrange
        val expected = "New Name"

        // Act
        val updatedInventoryItem = sut.changeName(expected)

        // Assert
        updatedInventoryItem.changes.shouldNotBeEmpty()
        updatedInventoryItem.changes.first().shouldBeTypeOf<InventoryItemCreated>()
        updatedInventoryItem.changes.first() as InventoryItemCreated shouldBe InventoryItemCreated(
            sut.baseEventInfo(),
            initialName,
            initialAvailableQuantity,
            initialMaxQuantity
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
})
