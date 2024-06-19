package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.simonschoof.tsmct.domain.InventoryItemDeactivated
import com.simonschoof.tsmct.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.tsmct.domain.InventoryItemNameChanged
import com.simonschoof.tsmct.domain.InventoryItemsCheckedIn
import com.simonschoof.tsmct.domain.InventoryItemsRemoved
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InventoryItemEventHandlers {

    @EventListener
    fun handle(notification: InventoryItemCreated) {
        println("created a new inventory item with name ${notification.name}")
    }

    @EventListener
    fun handle(notification: InventoryItemNameChanged) {
        println("changed name of inventory item to name ${notification.newName}")
    }

    @EventListener
    fun handle(notification: InventoryItemsCheckedIn) {
        println("checked in items for inventory item with ${notification.aggregateId}")
    }

    @EventListener
    fun handle(notification: InventoryItemsRemoved) {
        println("removed items for inventory item with ${notification.aggregateId}")
    }

    @EventListener
    fun handle(notification: InventoryItemMaxQuantityChanged) {
        println("changed max quantity of inventory item with ${notification.aggregateId} to ${notification.newMaxQuantity}")
    }

    @EventListener
    fun handle(notification: InventoryItemDeactivated) {
        println("deactivated inventory item with ${notification.aggregateId}")
    }
}