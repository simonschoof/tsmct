package com.simonschoof.tsmct.domain

interface EventStore {
    suspend fun saveEvents(aggregateId: AggregateId, events: Iterable<Event>)
    fun getEventsForAggregate(aggregateId: AggregateId): Iterable<Event>
}