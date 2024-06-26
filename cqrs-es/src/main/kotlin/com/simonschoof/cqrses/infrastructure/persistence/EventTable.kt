package com.simonschoof.cqrses.infrastructure.persistence

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.timestamp
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar

open class EventTable(alias: String?) : Table<Nothing>(
    tableName = "event",
    schema = "cqrs-es",
    alias = alias
) {
    companion object : EventTable(null)

    override fun aliased(alias: String) = EventTable(alias)
    val id = long("id").primaryKey()
    val aggregateType = varchar("aggregate_type")
    val eventType = varchar("event_type")
    val data = jsonb<Any>("data")
    val aggregateId = uuid("aggregate_id")
    val timestamp = timestamp("timestamp")
}