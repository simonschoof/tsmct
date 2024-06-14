package com.simonschoof.tsmct.domain.buildingblocks

interface EventStore {
    suspend fun saveEvents(aggregateId: AggregateId, aggregateType: String, events: Iterable<Event>)
    fun getEventsForAggregate(aggregateId: AggregateId): Iterable<Event>
}