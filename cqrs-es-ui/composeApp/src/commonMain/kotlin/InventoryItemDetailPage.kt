import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun DetailPage(aggregateId: String, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var item by remember { mutableStateOf<InventoryItemDetails?>(null) }

    LaunchedEffect(aggregateId) {
        coroutineScope.launch {
            val response = fetchItemDetails(aggregateId)
            item = Json.decodeFromString<InventoryItemDetails>(response)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Page") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (item != null) {
                Text(text = "Aggregate ID: ${item!!.aggregateId}", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Name: ${item!!.name}", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Available Quantity: ${item!!.availableQuantity}", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Max Quantity: ${item!!.maxQuantity}", textAlign = TextAlign.Center)
            } else {
                Text(text = "Loading...", textAlign = TextAlign.Center)
            }
        }
    }
}