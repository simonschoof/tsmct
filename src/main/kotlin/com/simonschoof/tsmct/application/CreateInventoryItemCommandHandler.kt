package com.simonschoof.tsmct.application

import com.simonschoof.tsmct.domain.AggregateRepository
import com.simonschoof.tsmct.domain.CreateInventoryItem
import com.simonschoof.tsmct.domain.InventoryItem
import com.trendyol.kediatr.CommandHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class CreateInventoryItemCommandHandler(private val aggregateRepository: AggregateRepository<InventoryItem>): CommandHandler<CreateInventoryItem> {
    override suspend fun handle(command: CreateInventoryItem) {
        val inventoryItem = InventoryItem.invoke(command.name)
        aggregateRepository.save(inventoryItem)
    }
}