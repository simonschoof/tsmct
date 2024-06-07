package com.simonschoof.tsmct.infrastructure

import com.simonschoof.tsmct.domain.AggregateId
import com.simonschoof.tsmct.domain.AggregateRepository
import com.simonschoof.tsmct.domain.AggregateRoot
import com.simonschoof.tsmct.domain.EventStore
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

    override fun getById(id: AggregateId): Optional<T> {

        val events = eventStore.getEventsForAggregate(id)

        if (events.count() == 0) {
            return Optional.empty()
        }

        val emptyAggregate =
            Class.forName(aggregateQualifiedNameProvider.getQualifiedNameBySimpleName(events.first().aggregateType))
                .kotlin.java.getDeclaredConstructor().newInstance() as T

        val aggregate = emptyAggregate.loadFromHistory(events)

        return Optional.of(aggregate)
    }

}