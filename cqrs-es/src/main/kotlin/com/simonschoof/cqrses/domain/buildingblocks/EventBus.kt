package com.simonschoof.cqrses.domain.buildingblocks

interface EventBus {
    fun publish(event: Event)
    fun send(command: Command)
}