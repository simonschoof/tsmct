package com.simonschoof.tsmct.domain

import java.util.Optional
import java.util.UUID


data class InventoryItem(
    override val id: Optional<AggregateId> = Optional.empty(),
    override val changes: MutableList<Event> = mutableListOf(),
    private val name: Optional<String> = Optional.empty(),
    private val isActivated: Boolean = false,
    private val availableQuantity: Int = 0,
    private val maxQuantity: Int = Int.MAX_VALUE,
) : AggregateRoot<InventoryItem> {

    override fun instantiateWithAggregateId(id: AggregateId): AggregateRoot<InventoryItem> =
        InventoryItem(id = Optional.of(id))

    override fun applyEvent(event: Event): InventoryItem = when (event) {
        is InventoryItemCreated -> copy(name = Optional.of(event.name), isActivated = true)
        else -> this
    }

    companion object {
        operator fun invoke(inventoryItemName: String): AggregateRoot<InventoryItem> {
            return InventoryItem(id = Optional.of(UUID.randomUUID()))
                .applyChange(InventoryItemCreated(inventoryItemName))
        }
    }

}