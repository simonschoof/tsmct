package com.simonschoof.cqrses.infrastructure.persistence

import com.simonschoof.cqrses.domain.InventoryItemNameChanged
import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
import com.simonschoof.cqrses.domain.buildingblocks.AggregateRoot
import com.simonschoof.cqrses.domain.buildingblocks.BaseEventInfo
import com.simonschoof.cqrses.domain.buildingblocks.Event
import java.time.Clock
import java.util.Optional

data class RepositoryTestAggregate(
    override val id: Optional<AggregateId> = Optional.empty(),
    override val changes: MutableList<Event> = mutableListOf(),
    override val clock: Clock = Clock.systemUTC(),
    private val name: Optional<String> = Optional.empty()
    ) : AggregateRoot<RepositoryTestAggregate> {

    override fun applyEvent(event: Event): RepositoryTestAggregate = when (event) {
        is RepositoryTestAggregateCreated -> copy(
            id = Optional.of(event.aggregateId),
            name = Optional.of(event.name)
        )
        is RepositoryTestAggregateNameChanged -> copy(name = Optional.of(event.newName))
        else -> this
    }

    companion object {
        operator fun invoke(name: String, clock: Clock = Clock.systemUTC()): RepositoryTestAggregate {

            val eventStoreTestAggregate = RepositoryTestAggregate(clock = clock)
            val event = RepositoryTestAggregateCreated(
                eventStoreTestAggregate.baseEventInfo(isNew = true),
                name
            )
            return eventStoreTestAggregate.applyChange(event)
        }
    }

    fun changeName(newName: String): RepositoryTestAggregate = applyChange(
        InventoryItemNameChanged(
            this.baseEventInfo(),
            newName = newName
        )
    )
}

data class RepositoryTestAggregateCreated(
    override val baseEventInfo: BaseEventInfo,
    val name: String) : Event

data class RepositoryTestAggregateNameChanged(
    override val baseEventInfo: BaseEventInfo,
    val newName: String
) : Event