package com.simonschoof.tsmct.domain

import java.util.UUID

interface AggregateRepository<T> {

    suspend fun save(aggregate: T)
    fun getById(id: UUID): AggregateRoot<T>

}