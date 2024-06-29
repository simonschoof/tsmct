package com.simonschoof.cqrses.domain

import com.simonschoof.cqrses.domain.buildingblocks.BaseEventInfo
import com.simonschoof.cqrses.domain.buildingblocks.Event


data class InventoryItemCreated(
    override val baseEventInfo: BaseEventInfo,
    val name: String,
    val availableQuantity: Int,
    val maxQuantity: Int
) : Event

data class InventoryItemNameChanged(
    override val baseEventInfo: BaseEventInfo,
    val newName: String
) : Event

data class InventoryItemsRemoved(
    override val baseEventInfo: BaseEventInfo,
    val count: Int,
    val newAvailableQuantity: Int
) : Event

data class InventoryItemsCheckedIn(
    override val baseEventInfo: BaseEventInfo,
    val count: Int,
    val newAvailableQuantity: Int
) : Event

data class InventoryItemMaxQuantityChanged(
    override val baseEventInfo: BaseEventInfo,
    val newMaxQuantity: Int
) : Event

class InventoryItemDeactivated(
    override val baseEventInfo: BaseEventInfo,
) : Event