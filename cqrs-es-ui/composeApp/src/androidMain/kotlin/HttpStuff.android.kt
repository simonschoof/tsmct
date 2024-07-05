import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

actual val httpClient: HttpClient
    get() = HttpClient(OkHttp)

actual fun fetchAndParseInventoryItems(): String {
    val response: String = runBlocking {
        fetchInventoryItems().body()
    }
    return response
}

actual suspend fun fetchInventoryItems(): HttpResponse {
    return httpClient.get("http://10.0.2.2:8080/api/inventoryItems").body()
}

@OptIn(InternalAPI::class)
actual suspend fun deleteItem(aggregateId: String) {
    httpClient.post("http://10.0.2.2:8080/api/deactivateInventoryItem") {
        contentType(ContentType.Application.Json)
        body = "{\"aggregateId\":\"$aggregateId\"}"
    }
}

actual suspend fun fetchItemDetails(aggregateId: String): String {
    val response: String = runBlocking {
        httpClient.get("http://10.0.2.2:8080/api/inventoryItemDetails/$aggregateId").body()
    }
    return response
}

@OptIn(InternalAPI::class)
actual suspend fun addItem(
    name: String,
    availableQuantity: Int,
    maxQuantity: Int
) {
    httpClient.post("http://10.0.2.2:8080/api/addInventoryItem") {
        contentType(ContentType.Application.Json)
        body = Json.encodeToString(InventoryItemDetails.serializer(), InventoryItemDetails("", name, availableQuantity, maxQuantity))
    }
}