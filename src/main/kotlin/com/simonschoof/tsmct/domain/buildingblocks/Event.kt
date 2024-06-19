package com.simonschoof.tsmct.domain.buildingblocks

import java.time.Instant


data class BaseEventInfo(
    val aggregateId: AggregateId,
    val aggregateType: String,
    val timestamp: Instant
)

interface Event  {
    val baseEventInfo: BaseEventInfo

    val aggregateId
        get() = baseEventInfo.aggregateId

    val aggregateType
        get() = baseEventInfo.aggregateType

    val timestamp
        get() = baseEventInfo.timestamp

}