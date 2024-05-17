package com.simonschoof.tsmct.infrastructure

import com.simonschoof.tsmct.domain.Event
import com.simonschoof.tsmct.domain.EventStore
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class KtormEventStore : EventStore {
    override fun saveEvents(aggregateId: UUID, events: Iterable<Event>) {
        TODO("Not yet implemented")
    }

    override fun getEventsForAggregate(aggregateId: UUID): Iterable<Event> {
        TODO("Not yet implemented")
    }
}