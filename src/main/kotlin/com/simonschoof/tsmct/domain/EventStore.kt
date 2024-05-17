package com.simonschoof.tsmct.domain

interface EventStore {
    fun saveEvents(aggregateId: AggregateId, events: Iterable<Event>)
    fun getEventsForAggregate(aggregateId: AggregateId): Iterable<Event>
}