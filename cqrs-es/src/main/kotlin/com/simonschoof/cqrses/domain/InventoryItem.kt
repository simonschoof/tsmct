package com.simonschoof.cqrses.domain

import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
import com.simonschoof.cqrses.domain.buildingblocks.AggregateRoot
import com.simonschoof.cqrses.domain.buildingblocks.Event
import java.time.Clock
import java.util.Optional

data class InventoryItem(
    override val id: Optional<AggregateId> = Optional.empty(),
    override val changes: MutableList<Event> = mutableListOf(),
    override val clock: Clock = Clock.systemUTC(),
    private val name: Optional<String> = Optional.empty(),
    private val isActivated: Boolean = false,
    private val availableQuantity: Int = 0,
    private val maxQuantity: Int = Int.MAX_VALUE,
) : AggregateRoot<InventoryItem> {

    override fun applyEvent(event: Event): InventoryItem = when (event) {
        is InventoryItemCreated -> copy(
            id = Optional.of(event.aggregateId),
            name = Optional.of(event.name),
            availableQuantity = event.availableQuantity,
            maxQuantity = event.maxQuantity,
            isActivated = true
        )
        is InventoryItemNameChanged -> copy(name = Optional.of(event.newName))
        is InventoryItemsRemoved -> copy(availableQuantity = event.newAvailableQuantity)
        is InventoryItemsCheckedIn -> copy(availableQuantity = event.newAvailableQuantity)
        is InventoryItemDeactivated -> copy(isActivated = false)
        else -> this
    }

    companion object {
        operator fun invoke(
            inventoryItemName: String,
            availableQuantity: Int,
            maxQuantity: Int,
            clock: Clock = Clock.systemUTC()): InventoryItem {
            val inventoryItem = InventoryItem(clock = clock)
            val event = InventoryItemCreated(
                inventoryItem.baseEventInfo(isNew = true),
                name = inventoryItemName,
                availableQuantity = availableQuantity,
                maxQuantity = maxQuantity
            )
            return inventoryItem.applyChange(event)
        }
    }

    fun changeName(newName: String): InventoryItem = applyChange(
        InventoryItemNameChanged(
            this.baseEventInfo(),
            newName = newName
        )
    )

    fun remove(count: Int): InventoryItem =
        if (count <= 0 || availableQuantity - count < 0) this
        else applyChange(
            InventoryItemsRemoved(
                this.baseEventInfo(),
                count,
                availableQuantity - count
            )
        )

    fun checkIn(count: Int): InventoryItem =
        if (count <= 0 || availableQuantity + count > maxQuantity) this
        else applyChange(
            InventoryItemsCheckedIn(
                this.baseEventInfo(),
                count,
                availableQuantity + count
            )
        )

    fun changeMaxQuantity(newMaxQuantity: Int): InventoryItem =
        if (newMaxQuantity <= 0 || newMaxQuantity < availableQuantity) this
        else applyChange(InventoryItemMaxQuantityChanged(this.baseEventInfo(), newMaxQuantity))

    fun deactivate(): InventoryItem =
        if (!isActivated) this
        else applyChange(InventoryItemDeactivated(this.baseEventInfo()))

}