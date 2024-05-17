package com.simonschoof.tsmct.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.simonschoof.tsmct.domain.Event
import com.simonschoof.tsmct.domain.EventStore
import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Instant
import java.util.UUID

@Component
class KtormEventStore(private val database: Database,
                      private val clock: Clock,
                      private val objectMapper: ObjectMapper,
                      private val e: EventTable = EventTable.aliased("e")) : EventStore {

    override fun saveEvents(aggregateId: UUID, events: Iterable<Event>) {
        events.forEach { event: Event -> saveEvent(aggregateId, event) }
    }

    override fun getEventsForAggregate(aggregateId: UUID): Iterable<Event> =
        database.from(e)
                .select()
                .where { e.aggregateUuid eq aggregateId }
                .orderBy(e.timestamp.desc())
                .map {
                    val clazz = Class.forName(it[e.eventType]!!).kotlin
                    val event = it[e.data]!! as String
                    objectMapper.readValue(event, clazz.javaObjectType) as Event
                }

    private fun saveEvent(aggregateId: UUID, event: Event) {
        database.insert(e) {
             set(e.eventType, event::class.simpleName)
             set(e.aggregateUuid, aggregateId)
             set(e.timestamp, Instant.now(clock))
             set(e.data, event)
         }
    }
}