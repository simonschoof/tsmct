package com.simonschoof.tsmct.infrastructure

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class AggregateQualifiedNameProvider(@Qualifier("AggregateRootClassNames") private val aggregateRootClassNames: Set<String>) {
    fun getQualifiedNameBySimpleName(simpleName: String): String {
        val qualifiedNames = aggregateRootClassNames.filter { it.contains(simpleName) }
        if (qualifiedNames.isEmpty()) {
            throw Error("No aggregate with $simpleName is registered")
        }
        return qualifiedNames.first()
    }
}

@Component
class EventQualifiedNameProvider(@Qualifier("EventClassNames") private val eventClassNames: Set<String>) {
    fun getQualifiedNameBySimpleName(simpleName: String): String {
        val qualifiedNames = eventClassNames.filter { it.contains(simpleName) }
        if (qualifiedNames.isEmpty()) {
            throw Error("No event with $simpleName is registered")
        }
        return qualifiedNames.first()
    }
}