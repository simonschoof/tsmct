package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.simonschoof.tsmct.domain.InventoryItemDeactivated
import com.simonschoof.tsmct.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.tsmct.domain.InventoryItemNameChanged
import com.simonschoof.tsmct.domain.InventoryItemsCheckedIn
import com.simonschoof.tsmct.domain.InventoryItemsRemoved
import com.trendyol.kediatr.NotificationHandler
import org.springframework.stereotype.Component

@Component
class InventoryItemCreatedEventHandler : NotificationHandler<InventoryItemCreated> {
    override suspend fun handle(notification: InventoryItemCreated) {
        println("created a new inventory item with name ${notification.name}")
    }
}

@Component
class InventoryItemNameChangedEventHandler : NotificationHandler<InventoryItemNameChanged> {
    override suspend fun handle(notification: InventoryItemNameChanged) {
        println("changed name of inventory item to name ${notification.newName}")
    }
}

@Component
class InventoryItemCheckedInEventHandler : NotificationHandler<InventoryItemsCheckedIn> {
    override suspend fun handle(notification: InventoryItemsCheckedIn) {
        println("checked in items for inventory item with ${notification.aggregateId}")
    }
}

@Component
class InventoryItemsRemovedEventHandler : NotificationHandler<InventoryItemsRemoved> {
    override suspend fun handle(notification: InventoryItemsRemoved) {
        println("removed items for inventory item with ${notification.aggregateId}")
    }
}

@Component
class InventoryItemMaxQuantityChangedEventHandler : NotificationHandler<InventoryItemMaxQuantityChanged> {
    override suspend fun handle(notification: InventoryItemMaxQuantityChanged) {
        println("changed max quantity of inventory item with ${notification.aggregateId} to ${notification.newMaxQuantity}")
    }
}

@Component
class InventoryItemDeactivatedEventHandler : NotificationHandler<InventoryItemDeactivated> {
    override suspend fun handle(notification: InventoryItemDeactivated) {
        println("deactivated inventory item with ${notification.aggregateId}")
    }
}