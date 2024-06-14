package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.ChangeInventoryItemName
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.InventoryItem
import com.simonschoof.tsmct.domain.buildingblocks.AggregateRepository
import com.trendyol.kediatr.CommandHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class CreateInventoryItemCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>):
    CommandHandler<CreateInventoryItem> {
    override suspend fun handle(command: CreateInventoryItem) {
        val inventoryItem = InventoryItem.invoke(command.name)
        aggregateRepository.save(inventoryItem)
    }
}

@Component
@Transactional
class ChangeInventoryItemNameCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>):
    CommandHandler<ChangeInventoryItemName> {
    override suspend fun handle(command: ChangeInventoryItemName) {
        val inventoryItem = aggregateRepository.getById(command.aggregateId)
        if(inventoryItem.isPresent) {
            aggregateRepository.save(inventoryItem.get().changeName(command.newName))
        }
    }
}


