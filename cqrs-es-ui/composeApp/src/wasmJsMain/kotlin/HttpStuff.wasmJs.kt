import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

actual val httpClient: HttpClient
    get() = HttpClient(Js)

actual fun fetchAndParseInventoryItems(): String = "Not yet implemented"

actual suspend fun fetchInventoryItems(): HttpResponse {
    return httpClient.get("http://localhost:8080/api/inventoryItems").body()
}

actual suspend fun deleteItem(aggregateId: String) {
}

actual suspend fun fetchItemDetails(aggregateId: String): String {
    TODO("Not yet implemented")
}