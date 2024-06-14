package com.simonschoof.tsmct.domain.buildingblocks

import java.time.Instant
import java.util.Optional
import java.util.UUID

typealias AggregateId = UUID

interface AggregateRoot<T> {

    val id: Optional<AggregateId>
    val changes: MutableList<Event>

    fun aggregateType(): String = this::class.simpleName!!

    fun applyChange(event: Event, isNew: Boolean = true): T {
        return applyEvent(event).apply { if (isNew) changes += event }
    }

    fun hasChanges() = changes.isNotEmpty()

    fun applyEvent(event: Event): T

    fun commitChanges() {
        changes.clear()
    }

    @Suppress("UNCHECKED_CAST")
    fun loadFromHistory(history: Iterable<Event>): T {
        return history.fold(
            initial = this,
            operation = ({ acc: AggregateRoot<T>, event: Event -> acc.applyChange(event, false) as AggregateRoot<T> })
        ) as T
    }

    fun baseEventInfo(isNew: Boolean = false): BaseEventInfo = BaseEventInfo(
        aggregateId = if (isNew) AggregateId.randomUUID() else this.id.get(),
        aggregateType = this.aggregateType(),
        timestamp = Instant.now()
    )
}