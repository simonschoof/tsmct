package com.simonschoof.tsmct.domain.buildingblocks

data class InventoryItemDto(
    val aggregateId: AggregateId,
    val name: String
)

data class InventoryItemDetailsDto(
    val aggregateId: AggregateId,
    val name: String,
    val availableQuantity: Int,
    val maxQuantity: Int,
    val isActive: Boolean
)