package com.simonschoof.tsmct.domain

import com.trendyol.kediatr.Notification
import java.time.Instant
import java.util.UUID

abstract class Event : Notification {
    var aggregateId: AggregateId = UUID.randomUUID()
    var aggregateType: String = ""
    var timestamp: Instant = Instant.now()

    companion object {
        fun getEventPath(): String {
            val qualifiedName = this::class.qualifiedName!!
            val indexOfLastDelimeter = qualifiedName.replace(".Companion", "").lastIndexOf('.')
            return qualifiedName.substring(0, indexOfLastDelimeter + 1)
        }
    }

    val test = Event::class.qualifiedName
}