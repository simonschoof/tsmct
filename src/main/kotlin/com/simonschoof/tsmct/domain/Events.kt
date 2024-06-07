package com.simonschoof.tsmct.domain

data class InventoryItemCreated(
    val name: String
) : Event()

data class InventoryItemNameChanged(
    val newName: String
): Event()

data class InventoryItemsRemoved(
    val count: Int
): Event()

data class InventoryItemsCheckedIn(
    val count: Int
): Event()

data class InventoryItemMaxQuantityChanged(
    val newMaxQuantity: Int
): Event()

class InventoryItemDeactivated : Event()