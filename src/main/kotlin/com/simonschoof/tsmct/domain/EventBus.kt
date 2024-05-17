package com.simonschoof.tsmct.domain

interface EventBus {
    fun send(event: Event)
    fun <R, C : Command> send(command: C): R
}