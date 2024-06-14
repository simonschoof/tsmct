package com.simonschoof.tsmct.infrastructure.web

import com.simonschoof.tsmct.domain.ChangeInventoryItemName
import com.simonschoof.tsmct.domain.ChangeMaxQuantity
import com.simonschoof.tsmct.domain.CheckInInventoryItems
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.DeactivateInventoryItem
import com.simonschoof.tsmct.domain.RemoveInventoryItems
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

    data class RemoveInventoryItemsRequest(val aggregateId: String, val count: Int)

    @PostMapping(
        value = ["/api/removeInventoryItems"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun removeInventoryItems(@RequestBody removeInventoryItemsRequest: RemoveInventoryItemsRequest) {
        val removeInventoryItems = RemoveInventoryItems(
            UUID.fromString(removeInventoryItemsRequest.aggregateId),
            removeInventoryItemsRequest.count
        )
        eventBus.send(removeInventoryItems)
    }

    data class CheckInInventoryItemsRequest(val aggregateId: String, val count: Int)

    @PostMapping(
        value = ["/api/checkInInventoryItems"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun checkInInventoryItems(@RequestBody checkInInventoryItemsRequest: CheckInInventoryItemsRequest) {
        val checkInInventoryItems = CheckInInventoryItems(
            UUID.fromString(checkInInventoryItemsRequest.aggregateId),
            checkInInventoryItemsRequest.count
        )
        eventBus.send(checkInInventoryItems)
    }

    data class ChangeMaxQuantityRequest(val aggregateId: String, val newMaxQuantity: Int)

    @PostMapping(
        value = ["/api/changeMaxQuantity"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun changeMaxQuantity(@RequestBody changeMaxQuantityRequest: ChangeMaxQuantityRequest) {
        val changeMaxQuantity = ChangeMaxQuantity(
            UUID.fromString(changeMaxQuantityRequest.aggregateId),
            changeMaxQuantityRequest.newMaxQuantity
        )
        eventBus.send(changeMaxQuantity)
    }

    data class DeactivateInventoryItemRequest(val aggregateId: String)

    @PostMapping(
        value = ["/api/deactivateInventoryItem"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun deactivateInventoryItem(@RequestBody deactivateInventoryItemRequest: DeactivateInventoryItemRequest) {
        val deactivateInventoryItem = DeactivateInventoryItem(UUID.fromString(deactivateInventoryItemRequest.aggregateId))
        eventBus.send(deactivateInventoryItem)
    }
}