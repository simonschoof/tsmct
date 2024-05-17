package com.simonschoof.tsmct.domain

import java.util.UUID

interface AggregateRoot<T> {

    val id: UUID
    val changes: MutableList<Event>

    fun create(): AggregateRoot<T>

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