package com.simonschoof.cqrses.infrastructure.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.simonschoof.cqrses.domain.ChangeInventoryItemName
import com.simonschoof.cqrses.domain.ChangeMaxQuantity
import com.simonschoof.cqrses.domain.CheckInInventoryItems
import com.simonschoof.cqrses.domain.CreateInventoryItem
import com.simonschoof.cqrses.domain.DeactivateInventoryItem
import com.simonschoof.cqrses.domain.RemoveInventoryItems
import com.simonschoof.cqrses.domain.buildingblocks.AggregateId
import com.simonschoof.cqrses.domain.buildingblocks.EventBus
import com.simonschoof.cqrses.infrastructure.web.InventoryItemController
import com.simonschoof.cqrses.readmodels.InventoryItemDetailsDto
import com.simonschoof.cqrses.readmodels.InventoryItemDto
import com.simonschoof.cqrses.readmodels.ReadModelFacade
import com.simonschoof.cqrses.WebSpec
import io.mockk.every
import io.mockk.verify
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

    private val aggregateIdStr = AggregateId.randomUUID().toString()

    init {

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

            verify { readModelFacade.getInventoryItems() }
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

            verify { readModelFacade.getInventoryItemDetails(id) }
        }

        test("POST /api/addInventoryItem") {
            // arrange
            val inventoryItemRequest = InventoryItemController.InventoryItemRequest("test", 5, 10)
            every { eventBus.send(any(CreateInventoryItem::class)) } returns Unit

            // act & assert
            mockMvc.perform(
                post("/api/addInventoryItem")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inventoryItemRequest))
            ).andExpect(status().isOk)

            verify(exactly = 1) { eventBus.send(any(CreateInventoryItem::class)) }

        }

        test("POST /api/checkInInventoryItems") {
            // arrange
            val checkInInventoryItemsRequest = InventoryItemController.CheckInInventoryItemsRequest(aggregateIdStr, 5)
            every { eventBus.send(any(CheckInInventoryItems::class)) } returns Unit

            // act & assert
            mockMvc.perform(
                post("/api/checkInInventoryItems")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(checkInInventoryItemsRequest))
            ).andExpect(status().isOk)

            verify(exactly = 1) { eventBus.send(any(CheckInInventoryItems::class))}
        }

        test("POST /api/changeInventoryItemName") {
            // arrange
            val changeNameRequest = InventoryItemController.ChangeInventoryItemNameRequest(aggregateIdStr, "newName")
            every { eventBus.send(any(ChangeInventoryItemName::class)) } returns Unit

            // act & assert
            mockMvc.perform(
                post("/api/changeInventoryItemName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changeNameRequest))
            ).andExpect(status().isOk)

            verify(exactly = 1) { eventBus.send(any(ChangeInventoryItemName::class))}

        }

        test("POST /api/removeInventoryItems") {
            // arrange
            val removeInventoryItemRequest = InventoryItemController.RemoveInventoryItemsRequest(aggregateIdStr, 5)
            every { eventBus.send(any(RemoveInventoryItems::class)) } returns Unit

            // act & assert
            mockMvc.perform(
                post("/api/removeInventoryItems")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(removeInventoryItemRequest))
            ).andExpect(status().isOk)

            verify(exactly = 1) { eventBus.send(any(RemoveInventoryItems::class)) }
        }

        test("POST /api/changeMaxQuantity") {
            // arrange
            val changeMaxQuantityRequest = InventoryItemController.ChangeMaxQuantityRequest(aggregateIdStr, 5)
            every { eventBus.send(any(ChangeMaxQuantity::class)) } returns Unit

            // act & assert
            mockMvc.perform(
                post("/api/changeMaxQuantity")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changeMaxQuantityRequest))
            ).andExpect(status().isOk)

            verify(exactly = 1) { eventBus.send(any(ChangeMaxQuantity::class)) }
        }

        test("POST /api/deactivateInventoryItem") {
            // arrange
            val deactivateInventoryItemRequest = InventoryItemController.DeactivateInventoryItemRequest(aggregateIdStr)
            every { eventBus.send(any(DeactivateInventoryItem::class)) } returns Unit

            // act & assert
            mockMvc.perform(
                post("/api/deactivateInventoryItem")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deactivateInventoryItemRequest))
            ).andExpect(status().isOk)

            verify(exactly = 1) { eventBus.send(any(DeactivateInventoryItem::class)) }
        }
    }
}
