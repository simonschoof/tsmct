package com.simonschoof.tsmct.domain

interface EventBus {
    suspend fun publish(event: Event)
    suspend fun send(command: Command)
}