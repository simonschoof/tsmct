package com.simonschoof.cqrses

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@WebMvcTest
@ActiveProfiles("test")
@ContextConfiguration(
    classes = [
        CqrsES::class
    ]
)
abstract class WebSpec : FunSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)
}