package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.ChangeInventoryItemName
import com.simonschoof.tsmct.domain.ChangeMaxQuantity
import com.simonschoof.tsmct.domain.CheckInInventoryItems
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.DeactivateInventoryItem
import com.simonschoof.tsmct.domain.InventoryItem
import com.simonschoof.tsmct.domain.RemoveInventoryItems
import com.simonschoof.tsmct.domain.buildingblocks.AggregateRepository
import com.trendyol.kediatr.CommandHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class CreateInventoryItemCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>) :
    CommandHandler<CreateInventoryItem> {
    override suspend fun handle(command: CreateInventoryItem) {
        val inventoryItem = InventoryItem.invoke(
            inventoryItemName = command.name,
            availableQuantity = command.availableQuantity,
            maxQuantity = command.maxQuantity
        )
        aggregateRepository.save(inventoryItem)
    }
}

@Component
@Transactional
class ChangeInventoryItemNameCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>) :
    CommandHandler<ChangeInventoryItemName> {
    override suspend fun handle(command: ChangeInventoryItemName) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().changeName(command.newName)
            if (updatedInventoryItem.changes.isNotEmpty()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }
}

@Component
@Transactional
class RemoveInventoryItemsCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>) :
    CommandHandler<RemoveInventoryItems> {
    override suspend fun handle(command: RemoveInventoryItems) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().remove(command.count)
            if (updatedInventoryItem.changes.isNotEmpty()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }
}

@Component
@Transactional
class CheckInInventoryItemsCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>) :
    CommandHandler<CheckInInventoryItems> {
    override suspend fun handle(command: CheckInInventoryItems) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().checkIn(command.count)
            if (updatedInventoryItem.changes.isNotEmpty()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }
}

@Component
@Transactional
class ChangeMaxQuantityCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>) :
    CommandHandler<ChangeMaxQuantity> {
    override suspend fun handle(command: ChangeMaxQuantity) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().changeMaxQuantity(command.newMaxQuantity)
            if (updatedInventoryItem.changes.isNotEmpty()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }
}

@Component
@Transactional
class DeactivateInventoryItemCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>) :
    CommandHandler<DeactivateInventoryItem> {
    override suspend fun handle(command: DeactivateInventoryItem) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if (inventoryItem.isPresent) {
            val updatedInventoryItem = inventoryItem.get().deactivate()
            if (updatedInventoryItem.changes.isNotEmpty()) {
                aggregateRepository.save(updatedInventoryItem)
            }
        }
    }
}




