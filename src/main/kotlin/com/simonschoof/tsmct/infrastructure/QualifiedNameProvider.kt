package com.simonschoof.tsmct.infrastructure

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class AggregateQualifiedNameProvider(@Qualifier("AggregateRootBeanNames") private val aggregateRootBeanNames: List<String>) {
    fun getQualifiedNameBySimpleName(simpleName: String): String {
        val qualifiedNames = aggregateRootBeanNames.filter { it.contains(simpleName) }
        if (qualifiedNames.isEmpty() || qualifiedNames.count() > 1) {
            throw Error("Multiple definitions for Aggragate name")
        }
        return qualifiedNames.first()
    }
}

@Component
class EventQualifiedNameProvider(@Qualifier("EventBeanNames") private val eventBeanNames: List<String>) {
    fun getQualifiedNameBySimpleName(simpleName: String): String {
        val qualifiedNames = eventBeanNames.filter { it.contains(simpleName) }
        if (qualifiedNames.isEmpty() || qualifiedNames.count() > 1) {
            throw Error("Multiple definitions for Event name")
        }
        return qualifiedNames.first()
    }
}