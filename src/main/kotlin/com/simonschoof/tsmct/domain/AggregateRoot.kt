package com.simonschoof.tsmct.domain

import java.util.Optional
import java.util.UUID

typealias AggregateId = UUID

interface AggregateRoot<T> {

    val id: Optional<UUID>
    val changes: MutableList<Event>

    fun instantiateWithAggregateId(id: AggregateId): AggregateRoot<T>

    fun applyChange(event: Event, isNew: Boolean = true): AggregateRoot<T> {
        return applyEvent(event).apply { if (isNew) changes += event }
    }

    fun hasChanges() = changes.isNotEmpty()

    fun applyEvent(event: Event): AggregateRoot<T>

    fun commitChanges() {
        changes.clear()
    }

    fun loadFromHistory(history: Iterable<Event>): AggregateRoot<T> {
        return history.fold(initial = this, operation = ( { acc: AggregateRoot<T>, event: Event  -> acc.applyChange(event, false) }))
    }
}