import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

expect val httpClient: HttpClient

expect suspend fun fetchInventoryItems(): HttpResponse

@Serializable
data class InventoryItem(val aggregateId: String, val name: String)

@Serializable
data class InventoryItemDetails(
    val aggregateId: String,
    val name: String,
    val availableQuantity: Int,
    val maxQuantity: Int,)
expect fun fetchAndParseInventoryItems(): String

expect suspend fun fetchItemDetails(aggregateId: String): String
expect suspend fun deleteItem(aggregateId: String)

expect suspend fun addItem(name: String, availableQuantity: Int, maxQuantity: Int)