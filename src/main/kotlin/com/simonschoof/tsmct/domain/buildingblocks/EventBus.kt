package com.simonschoof.tsmct.domain.buildingblocks

interface EventBus {
    suspend fun publish(event: Event)
    suspend fun send(command: Command)
}