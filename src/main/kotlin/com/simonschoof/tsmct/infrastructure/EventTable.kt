package com.simonschoof.tsmct.infrastructure

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.timestamp
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar

open class EventTable(alias: String?) : Table<Nothing>(
    tableName = "event",
    schema = "tsmct",
    alias = alias
) {
    companion object : EventTable(null)

    override fun aliased(alias: String) = EventTable(alias)
    val id = long("id").primaryKey()
    val aggregateType = varchar("aggregate_type")
    val eventType = varchar("event_type")
    val data = jsonb<Any>("data")
    val aggregateUuid = uuid("aggregate_uuid")
    val timestamp = timestamp("timestamp")
}