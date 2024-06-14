package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.simonschoof.tsmct.domain.InventoryItemNameChanged
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