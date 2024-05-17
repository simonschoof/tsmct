package com.simonschoof.tsmct.infrastructure

import com.simonschoof.tsmct.domain.AggregateRepository
import com.simonschoof.tsmct.domain.AggregateRoot
import com.simonschoof.tsmct.domain.EventStore
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class EventStoreAggregateRepository<T>(private val eventStore: EventStore) : AggregateRepository<T> {

    override fun save(aggregate: AggregateRoot<T>) {
        eventStore.saveEvents(aggregate.id, aggregate.changes)
    }

    override fun getById(id: UUID): AggregateRoot<T> {
        val aggregate = instantiate<AggregateRoot<T>>()
        val events = eventStore.getEventsForAggregate(aggregate.id)
        return aggregate.loadFromHistory(events)
    }

    private inline fun <reified T : Any> instantiate(): T {
        val createFunction = T::class.members.find { it.name == "create" }
        return createFunction!!.call() as T
    }
}