package com.simonschoof.tsmct.readmodels

import com.simonschoof.tsmct.domain.InventoryItemCreated
import com.simonschoof.tsmct.domain.InventoryItemDeactivated
import com.simonschoof.tsmct.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.tsmct.domain.InventoryItemNameChanged
import com.simonschoof.tsmct.domain.InventoryItemsCheckedIn
import com.simonschoof.tsmct.domain.InventoryItemsRemoved
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.update
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class InventoryItemListView(private val database: Database) {

    @EventListener
    fun handle(event: InventoryItemCreated) {
        println("created a new inventory item with name ${event.name}")
        database.sequenceOf(ReadModelInventoryItemTable).add(ReadModelInventoryItemEntity {
            aggregateId = event.aggregateId
            name = event.name
        })
    }

    @EventListener
    fun handle(event: InventoryItemNameChanged) {
        println("changed name of inventory item to name ${event.newName}")
        val readModelInventoryItemEntity = ReadModelInventoryItemEntity {
            aggregateId = event.aggregateId
            name = event.newName
        }
        database.sequenceOf(ReadModelInventoryItemTable).update(readModelInventoryItemEntity)
    }

    @EventListener
    fun handle(event: InventoryItemDeactivated) {
        println("deactivated inventory item with ${event.aggregateId}")
        database.sequenceOf(ReadModelInventoryItemTable).removeIf { it.aggregateId eq event.aggregateId }
    }

}

@Component
@Transactional
class InventoryItemDetailView(private val database: Database) {

    @EventListener
    fun handle(event: InventoryItemCreated) {
        println("created a new inventory item with name ${event.name}")
        database.sequenceOf(ReadModelInventoryItemDetailsTable).add(ReadModelInventoryItemDetailsEntity {
            aggregateId = event.aggregateId
            name = event.name
            availableQuantity = event.availableQuantity
            maxQuantity = event.maxQuantity
        })
    }

    @EventListener
    fun handle(event: InventoryItemNameChanged) {
        println("changed name of inventory item to name ${event.newName}")
        database.sequenceOf(ReadModelInventoryItemDetailsTable).find { it.aggregateId eq event.aggregateId }?.let {
            it.name = event.newName
            database.sequenceOf(ReadModelInventoryItemDetailsTable).update(it)
        }
    }

    @EventListener
    fun handle(event: InventoryItemsCheckedIn) {
        println("checked in items for inventory item with ${event.aggregateId}")
        database.sequenceOf(ReadModelInventoryItemDetailsTable).find { it.aggregateId eq event.aggregateId }?.let {
            it.availableQuantity = event.newAvailableQuantity
            database.sequenceOf(ReadModelInventoryItemDetailsTable).update(it)
        }
    }

    @EventListener
    fun handle(event: InventoryItemsRemoved) {
        println("removed items for inventory item with ${event.aggregateId}")
        database.sequenceOf(ReadModelInventoryItemDetailsTable).find { it.aggregateId eq event.aggregateId }?.let {
            it.availableQuantity = event.newAvailableQuantity
            database.sequenceOf(ReadModelInventoryItemDetailsTable).update(it)
        }
    }

    @EventListener
    fun handle(event: InventoryItemMaxQuantityChanged) {
        println("changed max quantity of inventory item with ${event.aggregateId} to ${event.newMaxQuantity}")
        database.sequenceOf(ReadModelInventoryItemDetailsTable).find { it.aggregateId eq event.aggregateId }?.let {
            it.maxQuantity = event.newMaxQuantity
            database.sequenceOf(ReadModelInventoryItemDetailsTable).update(it)
        }
    }

    @EventListener
    fun handle(event: InventoryItemDeactivated) {
        println("deactivated inventory item with ${event.aggregateId}")
        database.sequenceOf(ReadModelInventoryItemDetailsTable).removeIf { it.aggregateId eq event.aggregateId }
    }
}

