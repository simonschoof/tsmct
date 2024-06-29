package com.simonschoof.cqrses.application

import com.simonschoof.cqrses.domain.ChangeInventoryItemName
import com.simonschoof.cqrses.domain.ChangeMaxQuantity
import com.simonschoof.cqrses.domain.CheckInInventoryItems
import com.simonschoof.cqrses.domain.CreateInventoryItem
import com.simonschoof.cqrses.domain.DeactivateInventoryItem
import com.simonschoof.cqrses.domain.InventoryItem
import com.simonschoof.cqrses.domain.RemoveInventoryItems
import com.simonschoof.cqrses.domain.buildingblocks.AggregateRepository
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
        aggregateRepository.getById(command.aggregateId).ifPresent {
            it.changeName(command.newName).hasChanges().apply { aggregateRepository.save(it) }
        }
    }

    @EventListener
    fun handle(command: RemoveInventoryItems) {
        aggregateRepository.getById(command.aggregateId).ifPresent {
            it.remove(command.count).hasChanges().apply { aggregateRepository.save(it) }
        }
    }

    @EventListener
    fun handle(command: CheckInInventoryItems) {
        aggregateRepository.getById(command.aggregateId).ifPresent {
            it.checkIn(command.count).hasChanges().apply { aggregateRepository.save(it) }
        }
    }

    @EventListener
    fun handle(command: ChangeMaxQuantity) {
        aggregateRepository.getById(command.aggregateId).ifPresent {
            it.changeMaxQuantity(command.newMaxQuantity).hasChanges().apply { aggregateRepository.save(it) }
        }
    }

    @EventListener
    fun handle(command: DeactivateInventoryItem) {
        aggregateRepository.getById(command.aggregateId).ifPresent {
            it.deactivate().hasChanges().apply { aggregateRepository.save(it) }
        }
    }
}
