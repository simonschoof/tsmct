package com.simonschoof.tsmct.domain

import java.util.UUID

interface AggregateRoot<T> {

    val id: UUID
    val changes: MutableList<Event>

    fun applyChange(event: Event, isNew: Boolean = true): T {
        return applyEvent(event).apply { if (isNew) changes += event }
    }

    fun hasChanges() = changes.isNotEmpty()

    fun applyEvent(event: Event): T

    fun commitChanges() {
        changes.clear()
    }

    fun loadFromHistory(history: Iterable<Event>): T {
        return history.fold(initial = this, operation = ( { acc: AggregateRoot<T>, event: Event  -> acc.applyChange(event, false) as AggregateRoot<T>  })) as T
    }
}