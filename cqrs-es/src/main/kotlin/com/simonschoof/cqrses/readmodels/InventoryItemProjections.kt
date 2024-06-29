package com.simonschoof.cqrses.readmodels

import com.simonschoof.cqrses.domain.InventoryItemCreated
import com.simonschoof.cqrses.domain.InventoryItemDeactivated
import com.simonschoof.cqrses.domain.InventoryItemMaxQuantityChanged
import com.simonschoof.cqrses.domain.InventoryItemNameChanged
import com.simonschoof.cqrses.domain.InventoryItemsCheckedIn
import com.simonschoof.cqrses.domain.InventoryItemsRemoved
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.removeIf
import org.ktorm.entity.sequenceOf
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class InventoryItemListView(
    private val database: Database,
    private val rmiit: ReadModelInventoryItemTable = ReadModelInventoryItemTable.aliased("rmiit")
) {

    val Database.inventoryItems get() = this.sequenceOf(rmiit)

    @EventListener
    fun handle(event: InventoryItemCreated) {
        println("created a new inventory item with name ${event.name}")
        database.inventoryItems.add(ReadModelInventoryItemEntity {
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
        database.inventoryItems.find { it.aggregateId eq event.aggregateId }?.let {
            it.name = event.newName
            it.flushChanges()
        } ?: throw IllegalStateException("Inventory item with id ${event.aggregateId} not found")
    }

    @EventListener
    fun handle(event: InventoryItemDeactivated) {
        println("deactivated inventory item with ${event.aggregateId}")
        database.inventoryItems.removeIf { it.aggregateId eq event.aggregateId }
    }

}

@Component
@Transactional
class InventoryItemDetailView(
    private val database: Database,
    private val rmiidt: ReadModelInventoryItemDetailsTable = ReadModelInventoryItemDetailsTable.aliased("rmiidt")
) {

    val Database.inventoryItemDetails get() = this.sequenceOf(rmiidt)

    @EventListener
    fun handle(event: InventoryItemCreated) {
        println("created a new inventory item with name ${event.name}")
        database.inventoryItemDetails.add(ReadModelInventoryItemDetailsEntity {
            aggregateId = event.aggregateId
            name = event.name
            availableQuantity = event.availableQuantity
            maxQuantity = event.maxQuantity
        })
    }

    @EventListener
    fun handle(event: InventoryItemNameChanged) {
        println("changed name of inventory item to name ${event.newName}")
        database.inventoryItemDetails.find { it.aggregateId eq event.aggregateId }?.let {
            it.name = event.newName
            it.flushChanges()
        } ?: throw IllegalStateException("Inventory item with id ${event.aggregateId} not found")
    }

    @EventListener
    fun handle(event: InventoryItemsCheckedIn) {
        println("checked in items for inventory item with ${event.aggregateId}")
        database.inventoryItemDetails.find { it.aggregateId eq event.aggregateId }?.let {
            it.availableQuantity = event.newAvailableQuantity
            it.flushChanges()
        } ?: throw IllegalStateException("Inventory item with id ${event.aggregateId} not found")
    }

    @EventListener
    fun handle(event: InventoryItemsRemoved) {
        println("removed items for inventory item with ${event.aggregateId}")
        database.inventoryItemDetails.find { it.aggregateId eq event.aggregateId }?.let {
            it.availableQuantity = event.newAvailableQuantity
            it.flushChanges()
        } ?: throw IllegalStateException("Inventory item with id ${event.aggregateId} not found")
    }

    @EventListener
    fun handle(event: InventoryItemMaxQuantityChanged) {
        println("changed max quantity of inventory item with ${event.aggregateId} to ${event.newMaxQuantity}")
        database.inventoryItemDetails.find { it.aggregateId eq event.aggregateId }?.let {
            it.maxQuantity = event.newMaxQuantity
            it.flushChanges()
        } ?: throw IllegalStateException("Inventory item with id ${event.aggregateId} not found")
    }

    @EventListener
    fun handle(event: InventoryItemDeactivated) {
        println("deactivated inventory item with ${event.aggregateId}")
        database.inventoryItemDetails.removeIf { it.aggregateId eq event.aggregateId }
    }
}

