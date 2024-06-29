package com.simonschoof.cqrses.readmodels

import com.simonschoof.cqrses.domain.buildingblocks.AggregateId

data class InventoryItemDto(
    val aggregateId: AggregateId,
    val name: String
)

data class InventoryItemDetailsDto(
    val aggregateId: AggregateId,
    val name: String,
    val availableQuantity: Int,
    val maxQuantity: Int,
)