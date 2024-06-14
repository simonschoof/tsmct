package com.simonschoof.tsmct.domain

import com.trendyol.kediatr.Notification
import java.time.Instant


data class BaseEventInfo(
    val aggregateId: AggregateId,
    val aggregateType: String,
    val timestamp: Instant
)

interface Event : Notification {
    val baseEventInfo: BaseEventInfo

    val aggregateId
        get() = baseEventInfo.aggregateId

    val aggregateType
        get() = baseEventInfo.aggregateType

    val timestamp
        get() = baseEventInfo.timestamp

}