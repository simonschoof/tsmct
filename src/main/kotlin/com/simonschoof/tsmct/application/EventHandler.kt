package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.trendyol.kediatr.NotificationHandler
import org.springframework.stereotype.Component

@Component
class InventoryItemCreatedEventHandler : NotificationHandler<InventoryItemCreated> {
    override suspend fun handle(notification: InventoryItemCreated) {
        println("created a new inventory item with name ${notification.name}")
    }
}