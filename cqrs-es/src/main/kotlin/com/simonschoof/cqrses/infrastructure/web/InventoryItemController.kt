package com.simonschoof.cqrses.infrastructure.web


import com.simonschoof.cqrses.domain.ChangeInventoryItemName
import com.simonschoof.cqrses.domain.ChangeMaxQuantity
import com.simonschoof.cqrses.domain.CheckInInventoryItems
import com.simonschoof.cqrses.domain.CreateInventoryItem
import com.simonschoof.cqrses.domain.DeactivateInventoryItem
import com.simonschoof.cqrses.domain.RemoveInventoryItems
import com.simonschoof.cqrses.domain.buildingblocks.EventBus
import com.simonschoof.cqrses.readmodels.InventoryItemDetailsDto
import com.simonschoof.cqrses.readmodels.InventoryItemDto
import com.simonschoof.cqrses.readmodels.ReadModelFacade
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import java.util.UUID

private val logger = KotlinLogging.logger {}

@RestController
class InventoryItemController(
    private val eventBus: EventBus,
    private val readModelFacade: ReadModelFacade
) {

    data class InventoryItemRequest(val inventoryItemName: String, val availableQuantity: Int, val maxQuantity: Int)

    @PostMapping(
        value = ["/api/addInventoryItem"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addInventoryItem(@RequestBody inventoryItemRequest: InventoryItemRequest) {
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
    fun changeInventoryItemName(@RequestBody changeInventoryItemNameRequest: ChangeInventoryItemNameRequest) {
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
    fun removeInventoryItems(@RequestBody removeInventoryItemsRequest: RemoveInventoryItemsRequest) {
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
    fun checkInInventoryItems(@RequestBody checkInInventoryItemsRequest: CheckInInventoryItemsRequest) {
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
    fun changeMaxQuantity(@RequestBody changeMaxQuantityRequest: ChangeMaxQuantityRequest) {
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
    fun deactivateInventoryItem(@RequestBody deactivateInventoryItemRequest: DeactivateInventoryItemRequest) {
        val deactivateInventoryItem =
            DeactivateInventoryItem(UUID.fromString(deactivateInventoryItemRequest.aggregateId))
        eventBus.send(deactivateInventoryItem)
    }

    @GetMapping(
        value = ["/api/inventoryItems"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getInventoryItems() : List<InventoryItemDto> {
        return readModelFacade.getInventoryItems()
    }

    @GetMapping(
        value = ["/api/inventoryItemDetails/{aggregateId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getInventoryItemDetails(@PathVariable aggregateId: String): Optional<InventoryItemDetailsDto> {
        return readModelFacade.getInventoryItemDetails(UUID.fromString(aggregateId))
    }
}