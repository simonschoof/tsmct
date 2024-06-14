package com.simonschoof.tsmct.domain

data class InventoryItemCreated(
    override val baseEventInfo: BaseEventInfo,
    val name: String
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