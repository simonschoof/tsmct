package com.simonschoof.tsmct.infrastructure.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.Event
import com.simonschoof.tsmct.domain.buildingblocks.EventBus
import com.simonschoof.tsmct.domain.buildingblocks.EventStore
import com.simonschoof.tsmct.infrastructure.EventQualifiedNameProvider
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
class KtormEventStore(
    private val database: Database,
    private val e: EventTable = EventTable.aliased("e"),
    private val clock: Clock,
    private val objectMapper: ObjectMapper,
    private val eventBus: EventBus,
    private val eventQualifiedNameProvider: EventQualifiedNameProvider
) : EventStore {

    override suspend fun saveEvents(aggregateId: AggregateId, aggregateType: String, events: Iterable<Event>) {
        events.forEach { event: Event ->
            saveEvent(aggregateId, aggregateType, event)
            eventBus.publish(event)
        }
    }

    override fun getEventsForAggregate(aggregateId: AggregateId): Iterable<Event> =
        database.from(e)
            .select()
            .where { e.aggregateId eq aggregateId }
            .orderBy(e.timestamp.desc())
            .map {
                val clazz = Class.forName(eventQualifiedNameProvider.getQualifiedNameBySimpleName(it[e.eventType]!!)).kotlin
                val eventString = objectMapper.writeValueAsString(it[e.data]!! as LinkedHashMap<*, *>)
                val event = objectMapper.readValue(eventString, clazz.javaObjectType) as Event
                event
            }

    private fun saveEvent(aggregateId: AggregateId, aggregateType: String, event: Event) {
        database.insert(e) {
            set(e.eventType, event::class.simpleName)
            set(e.aggregateId, aggregateId)
            set(e.aggregateType, aggregateType)
            set(e.timestamp, Instant.now(clock))
            set(e.data, event)
        }
    }
}