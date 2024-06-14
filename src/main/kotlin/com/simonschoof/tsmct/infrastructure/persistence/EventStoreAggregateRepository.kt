package com.simonschoof.tsmct.infrastructure.persistence

import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.AggregateRepository
import com.simonschoof.tsmct.domain.buildingblocks.AggregateRoot
import com.simonschoof.tsmct.domain.buildingblocks.EventStore
import com.simonschoof.tsmct.infrastructure.AggregateQualifiedNameProvider
import org.springframework.stereotype.Component
import java.util.Optional


@Component
class EventStoreAggregateRepository<T : AggregateRoot<T>>(
    private val eventStore: EventStore,
    private val aggregateQualifiedNameProvider: AggregateQualifiedNameProvider
) : AggregateRepository<T> {

    override suspend fun save(aggregate: T) {
        if (aggregate.id.isPresent) {
            eventStore.saveEvents(
                aggregateId = aggregate.id.get(),
                aggregateType = aggregate.aggregateType(),
                events = aggregate.changes
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getById(id: AggregateId): Optional<T> {

        val events = eventStore.getEventsForAggregate(id)

        if (events.isEmpty()) {
            return Optional.empty()
        }

        val emptyAggregate =
            Class.forName(aggregateQualifiedNameProvider.getQualifiedNameBySimpleName(events.first().aggregateType))
                .kotlin.java.getDeclaredConstructor().newInstance() as T

        val aggregate = emptyAggregate.loadFromHistory(events)

        return Optional.of(aggregate)
    }

}