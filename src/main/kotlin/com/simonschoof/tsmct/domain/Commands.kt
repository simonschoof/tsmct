package com.simonschoof.tsmct.domain

data class CreateInventoryItem(
    val name: String
): Command

data class ChangeInventoryItemName(
    val aggregateId: AggregateId,
    val newName: String
): Command