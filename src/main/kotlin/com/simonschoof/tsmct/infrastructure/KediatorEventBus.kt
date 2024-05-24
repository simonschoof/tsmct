package com.simonschoof.tsmct.infrastructure

import com.simonschoof.tsmct.domain.Command
import com.simonschoof.tsmct.domain.Event
import com.simonschoof.tsmct.domain.EventBus
import com.trendyol.kediatr.Mediator
import org.springframework.stereotype.Component

@Component
class KediatorEventBus(private val mediator: Mediator) : EventBus {
     override suspend fun publish(event: Event) {
         mediator.publish(event)
     }

    override suspend fun send(command: Command) {
        return mediator.send(command)
    }
}