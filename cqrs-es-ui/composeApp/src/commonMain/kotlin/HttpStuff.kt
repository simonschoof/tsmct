import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

expect val httpClient: HttpClient

expect suspend fun fetchInventoryItems(): HttpResponse

@Serializable
data class InventoryItem(val aggregateId: String, val name: String)
expect fun fetchAndParseInventoryItems(): String
expect suspend fun deleteItem(aggregateId: String)