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
        is InventoryItemCreated -> copy(name = Optional.of(event.name), isActivated = true)
        else -> this
    }

    companion object {
        operator fun invoke(inventoryItemName: String): InventoryItem {
            return InventoryItem(id = Optional.of(AggregateId.randomUUID()))
                .applyChange(InventoryItemCreated(inventoryItemName))
        }
    }

}