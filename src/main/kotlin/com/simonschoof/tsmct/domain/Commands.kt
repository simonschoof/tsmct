package com.simonschoof.tsmct.domain

import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.Command

data class CreateInventoryItem(
    val name: String,
    val availableQuantity: Int,
    val maxQuantity: Int
): Command

data class ChangeInventoryItemName(
    val aggregateId: AggregateId,
    val newName: String
): Command

data class RemoveInventoryItems(
    val aggregateId: AggregateId,
    val count: Int
): Command

data class CheckInInventoryItems(
    val aggregateId: AggregateId,
    val count: Int
): Command

data class ChangeMaxQuantity(
    val aggregateId: AggregateId,
    val newMaxQuantity: Int
): Command

data class DeactivateInventoryItem(
    val aggregateId: AggregateId
): Command


