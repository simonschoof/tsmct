package com.simonschoof.cqrses.domain.buildingblocks

import java.time.Clock
import java.time.Instant
import java.util.Optional
import java.util.UUID

typealias AggregateId = UUID

interface AggregateRoot<T> where T : AggregateRoot<T> {

    val id: Optional<AggregateId>
    val changes: MutableList<Event>
    val clock: Clock

    fun aggregateType(): String = this::class.simpleName!!

    fun applyChange(event: Event, isNew: Boolean = true): T =
        applyEvent(event).apply { if (isNew) changes += event }

    fun hasChanges() = changes.isNotEmpty()

    fun applyEvent(event: Event): T

    fun commitChanges() = changes.clear()

    @Suppress("UNCHECKED_CAST")
    fun loadFromHistory(history: List<Event>): T =
        history.fold(this as T) { acc: T, event: Event ->
            acc.applyChange(event, false)
        }

    fun baseEventInfo(isNew: Boolean = false): BaseEventInfo = BaseEventInfo(
        aggregateId = if (isNew) AggregateId.randomUUID() else this.id.get(),
        aggregateType = this.aggregateType(),
        timestamp = Instant.now(clock)
    )
}