package io.ko.com.simonschoof.tsmct

import com.simonschoof.tsmct.Greeting
import com.simonschoof.tsmct.HelloWorldController
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class HelloWorldControllerTest : FunSpec({

    test("greeting should return greeting") {
     // Arrange
     val sut = HelloWorldController(null)
     val greetee = "tsmct"
     val expected = Greeting(1, "Hello, $greetee!")

     // Act
     val result = sut.greeting(greetee)

     // Assert
     expected shouldBe result
    }

    test("add should return the result of two given integer numbers") {
     // Arrange
     val sut = HelloWorldController(null)
     val expected = 9

     // Act
     val result = sut.add("4", "5")

     // Assert
     expected shouldBe result
    }
})
