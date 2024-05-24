package com.simonschoof.tsmct

import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.EventBus
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
class InventoryItemController(private val eventBus: EventBus) {

    data class InventoryItemRequest(val inventoryItemName: String)

    @PostMapping(
        value = ["/api/addInventoryItem"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun addInventoryItem(@RequestBody inventoryItemRequest: InventoryItemRequest){
        val createInventoryItem = CreateInventoryItem(inventoryItemRequest.inventoryItemName)
        eventBus.send(createInventoryItem)
    }
}