import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun InventoryList(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var refresh by remember { mutableStateOf(0) }
    var inventoryItemsJson by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(refresh) {
        coroutineScope.launch {
            val response = fetchInventoryItems().bodyAsText()
            inventoryItemsJson = response
        }
    }

    val inventoryItems: List<InventoryItem> =
        if (inventoryItemsJson.isBlank() || !inventoryItemsJson.startsWith("[")) {
            listOf()
        } else {
            try {
                Json.decodeFromString(inventoryItemsJson)
            } catch (e: Exception) {
                // Handle parsing error or log it
                listOf()
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventory Management") },
                backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = MaterialTheme.colors.onPrimary,
                elevation = 4.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Item")
            }
        }
    ) {
        Card {
            Column {
                // Table Header
                Row(modifier = Modifier.padding(8.dp)) {
                    Text("ID", modifier = Modifier.weight(2f), textAlign = TextAlign.Center)
                    Text("Name", modifier = Modifier.weight(3f), textAlign = TextAlign.Center)
                    Text("Actions", modifier = Modifier.weight(2f), textAlign = TextAlign.Center)
                }
                Divider()
                // Table Content
                LazyColumn {
                    items(inventoryItems) { item ->
                        InventoryItemRow(item, refresh, navController) { refresh++ }
                    }
                }
            }
        }
    }

    if (showDialog) {
        CheckInItemsDialog(
            onDismiss = { showDialog = false },
            onAddItem = { name, availableQuantity, maxQuantity ->
                coroutineScope.launch {
                    addItem(name, availableQuantity, maxQuantity) // Add the item
                    showDialog = false // Close the dialog
                    refresh++ // Refresh the list
                }
            }
        )
    }
}

@Composable
fun InventoryItemRow(
    item: InventoryItem,
    refresh: Int,
    navController: NavController,
    onRefresh: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Row(modifier = Modifier.padding(8.dp)) {
        Text(
            text = item.aggregateId,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
        Text(
            text = item.name,
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.weight(2f), horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = {
                navController.navigate("${MainDestinations.DETAIL_PAGE_ROUTE}/${item.aggregateId}")
            }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
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
}

@Composable
fun CheckInItemsDialog(onDismiss: () -> Unit, onAddItem: (String, Int, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var availableQuantity by remember { mutableStateOf(0) }
    var maxQuantity by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add new inventory item") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it.trim() },
                    label = { Text("Name") })
                IntegerInputWithArrows(
                    initialValue = availableQuantity,
                    onValueChange = { availableQuantity = it },
                    label = "Available quantity"
                )
                IntegerInputWithArrows(
                    initialValue = maxQuantity,
                    onValueChange = { maxQuantity = it },
                    label = "Max quantity"
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) {
                    onAddItem(name, availableQuantity, maxQuantity)
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun IntegerInputWithArrows(initialValue: Int = 0, label: String = "", onValueChange: (Int) -> Unit) {
    var value by remember { mutableStateOf(initialValue) }

    OutlinedTextField(
        value = value.toString(),
        onValueChange = { newValue ->
            newValue.toIntOrNull()?.let {
                value = it
                onValueChange(it)
            }
        },
        singleLine = true,
        label = { Text(label) },
        leadingIcon = {
            IconButton(onClick = { if (value > 0) {
                value--
                onValueChange(value)
            } }) {
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease")
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                value++
                onValueChange(value)
            }) {
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase")
            }
        }
    )
}

