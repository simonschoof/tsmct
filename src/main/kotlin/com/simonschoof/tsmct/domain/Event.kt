package com.simonschoof.tsmct.domain

import com.trendyol.kediatr.Notification
import java.time.Instant
import java.util.UUID

abstract class Event : Notification {
    var aggregateId: AggregateId = UUID.randomUUID()
    var aggregateType: String = ""
    var timestamp: Instant = Instant.now()
}