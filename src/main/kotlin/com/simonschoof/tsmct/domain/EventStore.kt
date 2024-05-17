package com.simonschoof.tsmct.domain

import java.util.UUID

interface EventStore {
    fun saveEvents(aggregateId: UUID, events: Iterable<Event>)
    fun getEventsForAggregate(aggregateId: UUID)
}