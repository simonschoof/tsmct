package com.simonschoof.tsmct.readmodels

import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.springframework.stereotype.Component
import java.util.Optional

interface ReadModelFacade {
    fun getInventoryItems(): List<InventoryItemDto>
    fun getInventoryItemDetails(aggregateId: AggregateId): Optional<InventoryItemDetailsDto>
}

@Component
class KtormReadModelFacade(private val database: Database): ReadModelFacade {
    override fun getInventoryItems(): List<InventoryItemDto> {
        return database.from(ReadModelInventoryItemTable)
            .select()
            .map {
                InventoryItemDto(
                    aggregateId = it[ReadModelInventoryItemTable.aggregateId]!!,
                    name = it[ReadModelInventoryItemTable.name]!!
                )
            }
    }

    override fun getInventoryItemDetails(aggregateId: AggregateId): Optional<InventoryItemDetailsDto> {
        return database.from(ReadModelInventoryItemDetailsTable)
            .select()
            .where { ReadModelInventoryItemDetailsTable.aggregateId eq aggregateId }
            .map {
                InventoryItemDetailsDto(
                    aggregateId = it[ReadModelInventoryItemDetailsTable.aggregateId]!!,
                    name = it[ReadModelInventoryItemDetailsTable.name]!!,
                    availableQuantity = it[ReadModelInventoryItemDetailsTable.availableQuantity]!!,
                    maxQuantity = it[ReadModelInventoryItemDetailsTable.maxQuantity]!!
                )
            }
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }
}



