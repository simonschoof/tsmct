package com.simonschoof.tsmct.domain

import java.util.UUID

data class InventoryItem(
    override val changes: MutableList<Event> = mutableListOf()) : AggregateRoot<InventoryItem> {

    override val id: UUID by lazy { this.id }
    override fun create(): AggregateRoot<InventoryItem> = InventoryItem()

    override fun applyEvent(event: Event): InventoryItem {
        TODO("Not yet implemented")
    }
}