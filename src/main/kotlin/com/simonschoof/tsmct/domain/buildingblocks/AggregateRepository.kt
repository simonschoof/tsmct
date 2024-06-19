package com.simonschoof.tsmct.domain.buildingblocks

import java.util.Optional

interface AggregateRepository<T: AggregateRoot<T>> {

    fun save(aggregate: T)
    fun getById(id: AggregateId): Optional<T>
}