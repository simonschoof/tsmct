package com.simonschoof.tsmct.configs

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.Event
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return objectMapper
    }
}

interface EventMixIn {
    @get:JsonIgnore
    val aggregateId: AggregateId
        get() = AggregateId.randomUUID()

    @get:JsonIgnore
    val aggregateType: String
        get() = ""

    @get:JsonIgnore
    val timestamp: Instant
        get() = Instant.now()
}


    val objectMapper: ObjectMapper = ObjectMapper().apply {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    registerModule(JavaTimeModule())
    registerModule(Jdk8Module())
    addMixIn(Event::class.java, EventMixIn::class.java)
    registerKotlinModule()
}