package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.ChangeInventoryItemName
import com.simonschoof.tsmct.domain.ChangeMaxQuantity
import com.simonschoof.tsmct.domain.CheckInInventoryItems
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.DeactivateInventoryItem
import com.simonschoof.tsmct.domain.InventoryItem
import com.simonschoof.tsmct.domain.RemoveInventoryItems
import com.simonschoof.tsmct.domain.buildingblocks.AggregateRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class InventoryItemCommandHandlers(private val aggregateRepository: AggregateRepository<InventoryItem>) {

    @EventListener
    fun handle(command: CreateInventoryItem) {
        val inventoryItem = InventoryItem.invoke(
            inventoryItemName = command.name,
            availableQuantity = command.availableQuantity,
            maxQuantity = command.maxQuantity
        )
        aggregateRepository.save(inventoryItem)
    }

    @EventListener
    fun handle(command: ChangeInventoryItemName) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().changeName(command.newName)
            if (updatedInventoryItem.hasChanges()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }

    @EventListener
    fun handle(command: RemoveInventoryItems) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().remove(command.count)
            if (updatedInventoryItem.hasChanges()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }

    @EventListener
    fun handle(command: CheckInInventoryItems) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().checkIn(command.count)
            if (updatedInventoryItem.hasChanges()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }

    @EventListener
    fun handle(command: ChangeMaxQuantity) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().changeMaxQuantity(command.newMaxQuantity)
            if (updatedInventoryItem.hasChanges()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }

    @EventListener
    fun handle(command: DeactivateInventoryItem) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().deactivate()
            if (updatedInventoryItem.hasChanges()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }
}
