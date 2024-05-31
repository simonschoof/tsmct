package com.simonschoof.tsmct.domain

interface AggregateRepository<T> {

    suspend fun save(aggregate: T)
    fun getById(id: AggregateId): AggregateRoot<T>

}