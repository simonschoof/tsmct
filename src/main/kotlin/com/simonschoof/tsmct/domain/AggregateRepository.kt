package com.simonschoof.tsmct.domain

import java.util.UUID

interface AggregateRepository<T> {

    fun save(aggregate: AggregateRoot<T>)
    fun getById(id: UUID): AggregateRoot<T>

}