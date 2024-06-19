package com.simonschoof.tsmct.domain.buildingblocks

interface EventBus {
    fun publish(event: Event)
    fun send(command: Command)
}