import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.Serializable

expect val httpClient: HttpClient

expect val localHost: String
@Serializable
data class InventoryItem(val aggregateId: String, val name: String)

@Serializable
data class InventoryItemDetails(
    val aggregateId: String,
    val name: String,
    val availableQuantity: Int,
    val maxQuantity: Int,)

suspend fun fetchInventoryItems(): HttpResponse {
    return httpClient.get("http://${localHost}:8080/api/inventoryItems")
}

@OptIn(InternalAPI::class)
suspend fun deleteItem(aggregateId: String) {
    httpClient.post("http://${localHost}:8080/api/deactivateInventoryItem") {
        contentType(ContentType.Application.Json)
        body = "{\"aggregateId\":\"$aggregateId\"}"
    }
}

suspend fun fetchItemDetails(aggregateId: String): String {
    return httpClient.get("http://${localHost}:8080/api/inventoryItemDetails/$aggregateId").body()
}

@OptIn(InternalAPI::class)
suspend fun addItem(
    name: String,
    availableQuantity: Int,
    maxQuantity: Int
) {
    httpClient.post("http://${localHost}:8080/api/addInventoryItem") {
        contentType(ContentType.Application.Json)
        body = "{\"inventoryItemName\":\"$name\",\"availableQuantity\":$availableQuantity,\"maxQuantity\":$maxQuantity}"
    }
}

@OptIn(InternalAPI::class)
suspend fun changeName(aggregateId: String, newName: String) {
    httpClient.post("http://${localHost}:8080/api/changeInventoryItemName") {
        contentType(ContentType.Application.Json)
        body = "{\"aggregateId\":\"$aggregateId\",\"newInventoryItemName\":\"$newName\"}"
    }
}

@OptIn(InternalAPI::class)
suspend fun changeMaxQuantity(aggregateId: String, newMaxQuantity: Int) {
    httpClient.post("http://${localHost}:8080/api/changeMaxQuantity") {
        contentType(ContentType.Application.Json)
        body = "{\"aggregateId\":\"$aggregateId\",\"newMaxQuantity\":$newMaxQuantity}"
    }
}

@OptIn(InternalAPI::class)
suspend fun removeItems(aggregateId: String, count: Int) {
    httpClient.post("http://${localHost}:8080/api/removeInventoryItems") {
        contentType(ContentType.Application.Json)
        body = "{\"aggregateId\":\"$aggregateId\",\"count\":$count}"
    }
}

@OptIn(InternalAPI::class)
suspend fun checkInItems(aggregateId: String, count: Int) {
    httpClient.post("http://${localHost}:8080/api/checkInInventoryItems") {
        contentType(ContentType.Application.Json)
        body = "{\"aggregateId\":\"$aggregateId\",\"count\":$count}"
    }
}

