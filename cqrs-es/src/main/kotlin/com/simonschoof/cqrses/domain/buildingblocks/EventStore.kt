package com.simonschoof.cqrses.domain.buildingblocks

interface EventStore {
    fun saveEvents(aggregateId: AggregateId, aggregateType: String, events: List<Event>)
    fun getEventsForAggregate(aggregateId: AggregateId): List<Event>
}