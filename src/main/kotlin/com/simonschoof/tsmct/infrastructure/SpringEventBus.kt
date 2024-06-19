package com.simonschoof.tsmct.infrastructure

import com.simonschoof.tsmct.domain.buildingblocks.Command
import com.simonschoof.tsmct.domain.buildingblocks.Event
import com.simonschoof.tsmct.domain.buildingblocks.EventBus
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