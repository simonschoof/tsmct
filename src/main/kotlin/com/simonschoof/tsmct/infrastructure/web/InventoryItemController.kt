package com.simonschoof.tsmct.infrastructure.web

import com.simonschoof.tsmct.domain.ChangeInventoryItemName
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.buildingblocks.EventBus
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

private val logger = KotlinLogging.logger {}

@RestController
class InventoryItemController(private val eventBus: EventBus) {

    data class InventoryItemRequest(val inventoryItemName: String, val availableQuantity: Int, val maxQuantity: Int)

    @PostMapping(
        value = ["/api/addInventoryItem"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun addInventoryItem(@RequestBody inventoryItemRequest: InventoryItemRequest) {
        val createInventoryItem = CreateInventoryItem(
            inventoryItemRequest.inventoryItemName,
            inventoryItemRequest.availableQuantity,
            inventoryItemRequest.maxQuantity
        )
        eventBus.send(createInventoryItem)
    }

    data class ChangeInventoryItemNameRequest(
        val aggregateId: String,
        val newInventoryItemName: String
    )

    @PostMapping(
        value = ["/api/changeInventoryItemName"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun changeInventoryItemName(@RequestBody changeInventoryItemNameRequest: ChangeInventoryItemNameRequest) {
        val changeInventoryItemName = ChangeInventoryItemName(
            UUID.fromString(changeInventoryItemNameRequest.aggregateId),
            changeInventoryItemNameRequest.newInventoryItemName
        )
        eventBus.send(changeInventoryItemName)
    }
}