package com.simonschoof.tsmct.readmodels

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar

interface ReadModelInventoryItemEntity: Entity<ReadModelInventoryItemEntity> {
    companion object : Entity.Factory<ReadModelInventoryItemEntity>()
    val id: Long
    var aggregateId: java.util.UUID
    var name: String
}

interface ReadModelInventoryItemDetailsEntity: Entity<ReadModelInventoryItemDetailsEntity> {
    companion object : Entity.Factory<ReadModelInventoryItemDetailsEntity>()
    val id: Long
    var aggregateId: java.util.UUID
    var name: String
    var availableQuantity: Int
    var maxQuantity: Int
}


open class ReadModelInventoryItemTable(alias: String?) : Table<ReadModelInventoryItemEntity>(
    tableName = "rm_inventory_item",
    schema = "tsmct",
    alias = alias
) {
    companion object : ReadModelInventoryItemTable(null)

    override fun aliased(alias: String) = ReadModelInventoryItemTable(alias)
    val id = long("id").primaryKey().bindTo { it.id }
    val aggregateId = uuid("aggregate_id").bindTo { it.aggregateId }
    val name = varchar("name").bindTo { it.name }
}

open class ReadModelInventoryItemDetailsTable(alias: String?) : Table<ReadModelInventoryItemDetailsEntity>(
    tableName = "rm_inventory_item_details",
    schema = "tsmct",
    alias = alias
) {
    companion object : ReadModelInventoryItemDetailsTable(null)

    override fun aliased(alias: String) = ReadModelInventoryItemDetailsTable(alias)
    val id = long("id").primaryKey().bindTo { it.id }
    val aggregateId = uuid("aggregate_id").bindTo { it.aggregateId }
    val name = varchar("name").bindTo { it.name }
    val availableQuantity = int("available_quantity").bindTo { it.availableQuantity }
    val maxQuantity = int("max_quantity").bindTo { it.maxQuantity }
}
