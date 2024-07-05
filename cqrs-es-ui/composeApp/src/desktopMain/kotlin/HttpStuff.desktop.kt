import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

actual val httpClient: HttpClient
    get() = HttpClient(OkHttp)

actual fun fetchAndParseInventoryItems(): String {
    val response: String = runBlocking {
        fetchInventoryItems().body()
    }
    return response
}

actual suspend fun fetchInventoryItems(): HttpResponse {
    return httpClient.get("http://localhost:8080/api/inventoryItems").body()
}

@OptIn(InternalAPI::class)
actual suspend fun deleteItem(aggregateId: String) {
    httpClient.post("http://localhost:8080/api/deactivateInventoryItem") {
        contentType(ContentType.Application.Json)
        body = "{\"aggregateId\":\"$aggregateId\"}"
    }
}

actual fun fetchItemDetails(aggregateId: String): String {
    TODO("Not yet implemented")
}