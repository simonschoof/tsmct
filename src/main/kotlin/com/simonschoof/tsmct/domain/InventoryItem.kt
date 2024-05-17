package com.simonschoof.tsmct.domain

import java.util.UUID

data class InventoryItem(
    override val id: UUID,
    override val changes: MutableList<Event> = mutableListOf()) : AggregateRoot<InventoryItem> {

    override fun applyEvent(event: Event): InventoryItem {
        TODO("Not yet implemented")
    }
}