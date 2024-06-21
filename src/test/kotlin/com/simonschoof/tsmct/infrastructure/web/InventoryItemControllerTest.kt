package io.ko.com.simonschoof.tsmct.infrastructure.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.simonschoof.tsmct.domain.buildingblocks.AggregateId
import com.simonschoof.tsmct.domain.buildingblocks.EventBus
import com.simonschoof.tsmct.infrastructure.web.InventoryItemController
import com.simonschoof.tsmct.readmodels.InventoryItemDetailsDto
import com.simonschoof.tsmct.readmodels.InventoryItemDto
import com.simonschoof.tsmct.readmodels.ReadModelFacade
import io.ko.com.simonschoof.tsmct.WebSpec
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.Optional

@ContextConfiguration(
    classes = [
        InventoryItemController::class
    ]
)
class InventoryItemControllerTest(
    private val inventoryItemController: InventoryItemController,
    private val objectMapper: ObjectMapper
) : WebSpec() {

    @MockkBean
    lateinit var readModelFacade: ReadModelFacade

    @MockkBean
    lateinit var eventBus: EventBus

    private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(inventoryItemController).build()

    init {

        test("initial controller test") {
            // arrange
            val inventoryItemRequest = InventoryItemController.InventoryItemRequest("test", 5, 10)
            every { eventBus.send(any()) } returns Unit
            // act & assert
            mockMvc.perform(
                post("/api/addInventoryItem")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inventoryItemRequest))
            )
                .andExpect(status().isOk)
        }

        test("GET /api/inventoryItems") {
            // Arrange
            val inventoryItemDto = InventoryItemDto(AggregateId.randomUUID(), "test")
            val inventoryItemDto2 = InventoryItemDto(AggregateId.randomUUID(), "test2")
            val expectedInventoryItems = listOf(inventoryItemDto, inventoryItemDto2)
            every { readModelFacade.getInventoryItems() } returns expectedInventoryItems

            // Act & Assert
            mockMvc.perform(get("/api/inventoryItems"))
                .andExpect(status().isOk)
                .andExpect(content().json(objectMapper.writeValueAsString(expectedInventoryItems)))
        }

        test("GET /api/inventoryItemDetails/{id}") {
            // Arrange
            val id = AggregateId.randomUUID()
            val expectedInventoryItem = InventoryItemDetailsDto(id, "test", 5, 10)
                every { readModelFacade.getInventoryItemDetails(id) } returns Optional.of(expectedInventoryItem)

            // Act & Assert
            mockMvc.perform(get("/api/inventoryItemDetails?aggregateId=$id"))
                .andExpect(status().isOk)
                .andExpect(content().json(objectMapper.writeValueAsString(expectedInventoryItem)))
        }

        test("POST /api/addInventoryItem") {
            // arrange
            val inventoryItemRequest = InventoryItemController.InventoryItemRequest("test", 5, 10)
            every { eventBus.send(any()) } returns Unit

            // act & assert
            mockMvc.perform(
                post("/api/addInventoryItem")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inventoryItemRequest))
            )

        }
    }
}
