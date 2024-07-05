import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InventoryList(navController: NavController) {
    var refresh by remember { mutableStateOf(0) }
    var inventoryItemsJson by remember { mutableStateOf(fetchAndParseInventoryItems()) }

    // Re-fetch the data whenever refresh changes
    LaunchedEffect(refresh) {
        inventoryItemsJson = fetchAndParseInventoryItems()
    }

    val inventoryItems: List<InventoryItem> = Json.decodeFromString(inventoryItemsJson)

    Card {
        Column {
            // Header
            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = "Aggregate ID", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Name", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }

            // Create a LazyColumn
            LazyColumn {
                // Create a row for each item
                items(inventoryItems) { item ->
                    InventoryItemRow(item, refresh, navController) { refresh++ }
                }
            }
        }
    }
}

@Composable
fun InventoryItemRow(item: InventoryItem, refresh: Int, navController: NavController, onRefresh: () -> Unit) {
val coroutineScope = rememberCoroutineScope()
    // Display the item in a row
    Row(modifier = Modifier.padding(8.dp)) {
        Text(
            text = item.aggregateId,
            modifier = Modifier.weight(1f).clickable { /* Handle aggregateId click event here */ },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = item.name,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = {
            navController.navigate("${MainDestinations.DETAIL_PAGE_ROUTE}/${item.aggregateId}")
        }) {
            Icon(Icons.Filled.Edit, contentDescription = "Detail Page")
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = {
            coroutineScope.launch {
                deleteItem(item.aggregateId)
                onRefresh()
            }
        }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}

//@Preview
//@Composable
//fun PreviewInventoryList() {
//    val inventoryItemsJson =
//        "[{\"aggregateId\":\"uuid\",\"name\":\"name\"},{\"aggregateId\":\"uuid2\",\"name\":\"name2\"}]"
//    InventoryList(NavController())
//}