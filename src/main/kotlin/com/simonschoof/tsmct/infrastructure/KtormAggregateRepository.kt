package com.simonschoof.tsmct.infrastructure

//import com.simonschoof.tsmct.domain.AggregateRepository
//import com.simonschoof.tsmct.domain.AggregateRoot
//import com.simonschoof.tsmct.domain.EventStore
//import org.springframework.stereotype.Component
//import java.util.UUID
//
//@Component
//class KtormAggregateRepository<T>(private val eventStore: EventStore) : AggregateRepository<T> {
//
//    override fun save(aggregate: AggregateRoot<T>) {
//        eventStore.saveEvents(aggregate.id, aggregate.changes)
//    }
//
//    override fun getById(id: UUID): T {
//      Any
//    }
//}