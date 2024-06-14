package com.simonschoof.tsmct.domain

import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.Command

data class CreateInventoryItem(
    val name: String
): Command

data class ChangeInventoryItemName(
    val aggregateId: AggregateId,
    val newName: String
): Command