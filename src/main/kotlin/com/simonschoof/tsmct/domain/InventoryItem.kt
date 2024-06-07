package com.simonschoof.tsmct.domain

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
        is InventoryItemCreated -> copy(id=Optional.of(event.aggregateId), name = Optional.of(event.name), isActivated = true)
        is InventoryItemNameChanged -> copy(name = Optional.of(event.newName))
        is InventoryItemsRemoved -> copy(availableQuantity = availableQuantity - event.count)
        is InventoryItemsCheckedIn -> copy(availableQuantity = availableQuantity + event.count)
        is InventoryItemDeactivated -> copy(isActivated = false)
        else -> this
    }

    companion object {
        operator fun invoke(inventoryItemName: String): InventoryItem {
            return InventoryItem(id = Optional.of(AggregateId.randomUUID()))
                .applyChange(InventoryItemCreated(inventoryItemName))
        }
    }

    fun changeName(newName: String): InventoryItem = applyChange(InventoryItemNameChanged(newName))

    fun remove(count: Int): InventoryItem =
        if (count <= 0 || availableQuantity - count < 0) this
        else applyChange(InventoryItemsRemoved(count))

    fun checkIn(count: Int): InventoryItem =
        if (count <= 0 || availableQuantity + count > maxQuantity) this
        else applyChange(InventoryItemsCheckedIn(count))

    fun changeMaxQuantity(newMaxQuantity: Int): InventoryItem =
        if (newMaxQuantity <= 0 || newMaxQuantity < availableQuantity) this
        else applyChange(InventoryItemMaxQuantityChanged(newMaxQuantity))

    fun deactivate(): InventoryItem =
        if (!isActivated) this
        else applyChange(InventoryItemDeactivated())

}