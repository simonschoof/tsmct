package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.InventoryItemDetailsDto
import com.simonschoof.tsmct.domain.buildingblocks.InventoryItemDto
import java.util.Optional

interface ReadModelFacade {
    fun getInventoryItems(): List<InventoryItemDto>
    fun getInventoryItemDetails(aggregateId: AggregateId): Optional<InventoryItemDetailsDto>
}




