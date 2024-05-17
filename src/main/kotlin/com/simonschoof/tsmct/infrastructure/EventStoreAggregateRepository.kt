package com.simonschoof.tsmct.infrastructure

import com.simonschoof.tsmct.domain.AggregateId
import com.simonschoof.tsmct.domain.AggregateRepository
import com.simonschoof.tsmct.domain.AggregateRoot
import com.simonschoof.tsmct.domain.EventStore
import org.springframework.stereotype.Component

@Component
class EventStoreAggregateRepository<T>(private val eventStore: EventStore) : AggregateRepository<T> {

    override fun save(aggregate: AggregateRoot<T>) {
        aggregate.id.ifPresent {
            eventStore.saveEvents(aggregateId = aggregate.id.get(), events = aggregate.changes)
        }
    }

    override fun getById(id: AggregateId): AggregateRoot<T> {
        val aggregate = instantiateWithAggregateId<AggregateRoot<T>>(id)

        aggregate.id.ifPresent {
            val events = eventStore.getEventsForAggregate(aggregate.id.get())
            aggregate.loadFromHistory(events)
        }

        return aggregate
    }

    private inline fun <reified T : Any> instantiateWithAggregateId(aggregateId: AggregateId): T {
        val createFunction = T::class.members.find { it.name == "create" }
        return createFunction!!.call(aggregateId) as T
    }
}