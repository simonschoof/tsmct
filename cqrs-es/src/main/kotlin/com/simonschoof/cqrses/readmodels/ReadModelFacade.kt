package com.simonschoof.cqrses.readmodels

import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
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
class KtormReadModelFacade(
    private val database: Database,
    private val rmiit: ReadModelInventoryItemTable = ReadModelInventoryItemTable.aliased("rmiit"),
    private val rmiidt: ReadModelInventoryItemDetailsTable = ReadModelInventoryItemDetailsTable.aliased("rmiidt")
) : ReadModelFacade {

    override fun getInventoryItems(): List<InventoryItemDto> {
        return database.from(rmiit)
            .select()
            .map {
                InventoryItemDto(
                    aggregateId = it[rmiit.aggregateId]!!,
                    name = it[rmiit.name]!!
                )
            }
    }

    override fun getInventoryItemDetails(aggregateId: AggregateId): Optional<InventoryItemDetailsDto> {
        return database.from(rmiidt)
            .select()
            .where { rmiidt.aggregateId eq aggregateId }
            .map {
                InventoryItemDetailsDto(
                    aggregateId = it[rmiidt.aggregateId]!!,
                    name = it[rmiidt.name]!!,
                    availableQuantity = it[rmiidt.availableQuantity]!!,
                    maxQuantity = it[rmiidt.maxQuantity]!!
                )
            }
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }
}



