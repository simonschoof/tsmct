package com.simonschoof.tsmct.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.simonschoof.tsmct.domain.AggregateId
import com.simonschoof.tsmct.domain.Event
import com.simonschoof.tsmct.domain.EventBus
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

@Component
class KtormEventStore(private val database: Database,
                      private val e: EventTable = EventTable.aliased("e"),
                      private val clock: Clock,
                      private val objectMapper: ObjectMapper,
                      private val eventBus: EventBus) : EventStore {

    override suspend fun saveEvents(aggregateId: AggregateId, events: Iterable<Event>) {
        events.forEach {
            event: Event -> saveEvent(aggregateId, event)
            eventBus.publish(event)
        }
    }

    override fun getEventsForAggregate(aggregateId: AggregateId): Iterable<Event> =
        database.from(e)
                .select()
                .where { e.aggregateUuid eq aggregateId }
                .orderBy(e.timestamp.desc())
                .map {
                    val clazz = Class.forName(it[e.eventType]!!).kotlin
                    val event = it[e.data]!! as String
                    objectMapper.readValue(event, clazz.javaObjectType) as Event
                }

    private fun saveEvent(aggregateId: AggregateId, event: Event) {
        database.insert(e) {
             set(e.eventType, event::class.simpleName)
             set(e.aggregateUuid, aggregateId)
             set(e.timestamp, Instant.now(clock))
             set(e.data, event)
         }
    }
}