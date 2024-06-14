package com.simonschoof.tsmct.domain

import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.AggregateRoot
import com.simonschoof.tsmct.domain.buildingblocks.BaseEventInfo
import com.simonschoof.tsmct.domain.buildingblocks.Event
import com.simonschoof.tsmct.domain.buildingblocks.baseEventInfo
import java.time.Instant
import java.util.Optional

data class InventoryItem(
    override val id: Optional<AggregateId> = Optional.empty(),
    override val changes: MutableList<Event> = mutableListOf(),
    private val name: Optional<String> = Optional.empty(),
    private val isActivated: Boolean = false,
    private val availableQuantity: Int = 0,
    private val maxQuantity: Int = Int.MAX_VALUE,
) : AggregateRoot<InventoryItem> {

    override fun applyEvent(event: Event): InventoryItem = when (event) {
        is InventoryItemCreated -> copy(
            id = Optional.of(event.aggregateId),
            name = Optional.of(event.name),
            isActivated = true
        )
        is InventoryItemNameChanged -> copy(name = Optional.of(event.newName))
        is InventoryItemsRemoved -> copy(availableQuantity = event.newAvailableQuantity)
        is InventoryItemsCheckedIn -> copy(availableQuantity = event.newAvailableQuantity)
        is InventoryItemDeactivated -> copy(isActivated = false)
        else -> this
    }

    companion object {
        operator fun invoke(inventoryItemName: String): InventoryItem {
            val event = InventoryItemCreated(
                BaseEventInfo(
                    aggregateId = AggregateId.randomUUID(),
                    aggregateType = InventoryItem::class.simpleName!!,
                    timestamp = Instant.now()
                ),
                name = inventoryItemName
            )
            return InventoryItem()
                .applyChange(event)
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