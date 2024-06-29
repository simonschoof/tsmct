package com.simonschoof.cqrses.infrastructure

import com.simonschoof.cqrses.domain.buildingblocks.Command
import com.simonschoof.cqrses.domain.buildingblocks.Event
import com.simonschoof.cqrses.domain.buildingblocks.EventBus
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SpringEventBus(val publisher: ApplicationEventPublisher): EventBus {

    override fun publish(event: Event) {
        publisher.publishEvent(event)
    }

    override fun send(command: Command) {
        publisher.publishEvent(command)
    }

}