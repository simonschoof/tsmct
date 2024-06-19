package io.ko.com.simonschoof.tsmct

import com.simonschoof.tsmct.TsmctApplication
import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@WebMvcTest
@ActiveProfiles("test")
@ContextConfiguration(
    classes = [
        TsmctApplication::class
    ]
)
abstract class WebSpec(body: FunSpec.() -> Unit) : FunSpec(body)