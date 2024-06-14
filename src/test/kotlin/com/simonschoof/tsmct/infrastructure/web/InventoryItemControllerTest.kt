package io.ko.com.simonschoof.tsmct.infrastructure.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.simonschoof.tsmct.infrastructure.KediatorEventBus
import com.simonschoof.tsmct.infrastructure.web.InventoryItemController
import io.ko.com.simonschoof.tsmct.WebSpec
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ContextConfiguration(
    classes = [
        InventoryItemController::class,
        KediatorEventBus::class,
    ]
)
class InventoryItemControllerTest(
    private val inventoryItemController: InventoryItemController,
    private val objectMapper: ObjectMapper
) : WebSpec({

    val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(inventoryItemController).build()

    test("initial controller test") {
        // arrange
        val inventoryItemRequest = InventoryItemController.InventoryItemRequest("test", 5, 10)

        // act & assert
        mockMvc.perform(post("/api/addInventoryItem")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inventoryItemRequest)))
            .andExpect(status().isOk)
    }
})
