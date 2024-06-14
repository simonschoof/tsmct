package com.simonschoof.tsmct.domain.buildingblocks

interface EventStore {
    suspend fun saveEvents(aggregateId: AggregateId, aggregateType: String, events: List<Event>)
    fun getEventsForAggregate(aggregateId: AggregateId): List<Event>
}