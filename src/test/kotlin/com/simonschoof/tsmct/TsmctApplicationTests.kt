package com.simonschoof.tsmct

import io.kotest.core.spec.style.ShouldSpec
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TsmctApplicationTests: ShouldSpec() {

	init {
		this.should("load the spring context") {
		}
	}
}
