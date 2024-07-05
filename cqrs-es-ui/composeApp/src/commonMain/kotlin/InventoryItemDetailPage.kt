import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun DetailPage(aggregateId: String, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var item by remember { mutableStateOf<InventoryItemDetails?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var showChangeNameDialog by remember { mutableStateOf(false) }
    var showRemoveItemsDialog by remember { mutableStateOf(false) }
    var showCheckInItemsDialog by remember { mutableStateOf(false) }
    var showChangeMaxQuantityDialog by remember { mutableStateOf(false) }

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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { showMenu = !showMenu }) {
                        Text("Actions", color = MaterialTheme.colors.onPrimary)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            showChangeNameDialog = true
                            showMenu = false
                        }) {
                            Text("Change Name")
                        }
                        DropdownMenuItem(onClick = {
                            showRemoveItemsDialog = true
                            showMenu = false
                        }) {
                            Text("Remove Inventory Items")
                        }
                        DropdownMenuItem(onClick = {
                            showCheckInItemsDialog = true
                            showMenu = false
                        }) {
                            Text("Check in Inventory Items")
                        }
                        DropdownMenuItem(onClick = {
                            showChangeMaxQuantityDialog = true
                            showMenu = false
                        }) {
                            Text("Change Max Quantity")
                        }
                        DropdownMenuItem(onClick = { /* Handle deactivate */ }) {
                            Text("Deactivate")
                        }
                    }
                }
            )
        }
    ) {
        Card(
            elevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (item != null) {
                    Text(text = "Aggregate ID: ${item!!.aggregateId}", textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Name: ${item!!.name}", textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Available Quantity: ${item!!.availableQuantity}",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Max Quantity: ${item!!.maxQuantity}", textAlign = TextAlign.Center)
                } else {
                    Text(text = "Loading...", textAlign = TextAlign.Center)
                }
            }
        }
    }

    if (showChangeNameDialog) {
        ChangeNameDialog(
            aggregateId = aggregateId,
            showDialog = showChangeNameDialog,
            onDismiss = { showChangeNameDialog = false },
            refreshDetailView = {
                coroutineScope.launch {
                    val response = fetchItemDetails(aggregateId)
                    item = Json.decodeFromString<InventoryItemDetails>(response)
                }
            }
        )
    }

    if (showChangeMaxQuantityDialog) {
        ChangeMaxQuantityDialog(
            aggregateId = aggregateId,
            showDialog = showChangeMaxQuantityDialog,
            onDismiss = { showChangeMaxQuantityDialog = false },
            refreshDetailView = {
                coroutineScope.launch {
                    val response = fetchItemDetails(aggregateId)
                    item = Json.decodeFromString<InventoryItemDetails>(response)
                }
            }
        )
    }

    if (showRemoveItemsDialog) {
        RemoveItemsDialog(
            aggregateId = aggregateId,
            showDialog = showRemoveItemsDialog,
            onDismiss = { showRemoveItemsDialog = false },
            refreshDetailView = {
                coroutineScope.launch {
                    val response = fetchItemDetails(aggregateId)
                    item = Json.decodeFromString<InventoryItemDetails>(response)
                }
            }
        )
    }

    if (showCheckInItemsDialog) {
        CheckInItemsDialog(
            aggregateId = aggregateId,
            showDialog = showCheckInItemsDialog,
            onDismiss = { showCheckInItemsDialog = false },
            refreshDetailView = {
                coroutineScope.launch {
                    val response = fetchItemDetails(aggregateId)
                    item = Json.decodeFromString<InventoryItemDetails>(response)
                }
            }
        )
    }
}

@Composable
fun ChangeNameDialog(
    aggregateId: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    refreshDetailView: suspend () -> Unit
) {
    var newName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Change Name") },
            text = {
                TextField(
                    value = newName,
                    onValueChange = { newName = it.trim() },
                    label = { Text("New Name") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        if (newName.isNotBlank()) {
                            changeName(aggregateId, newName)
                            onDismiss()
                            refreshDetailView()
                        }
                    }
                }) {
                    Text("Change")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RemoveItemsDialog(
    aggregateId: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    refreshDetailView: suspend () -> Unit
) {
    var itemsToRemoveText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Remove Items") },
            text = {
                TextField(
                    value = itemsToRemoveText,
                    onValueChange = { itemsToRemoveText = it },
                    label = { Text("Items to Remove") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        val itemsToRemove = itemsToRemoveText.toIntOrNull() ?: 0
                        val validatedItemsToRemove = maxOf(itemsToRemove, 0)
                        removeItems(aggregateId, validatedItemsToRemove)
                        onDismiss()
                        refreshDetailView()
                    }
                }) {
                    Text("Remove")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CheckInItemsDialog(
    aggregateId: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    refreshDetailView: suspend () -> Unit
) {
    var itemsToCheckInText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Check In Items") },
            text = {
                TextField(
                    value = itemsToCheckInText,
                    onValueChange = { itemsToCheckInText = it },
                    label = { Text("Items to Check In") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        val itemsToCheckIn = itemsToCheckInText.toIntOrNull() ?: 0
                        val validatedItemsToCheckIn = maxOf(itemsToCheckIn, 0)
                        checkInItems(aggregateId, validatedItemsToCheckIn)
                        onDismiss()
                        refreshDetailView()
                    }
                }) {
                    Text("Check In")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ChangeMaxQuantityDialog(
    aggregateId: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    refreshDetailView: suspend () -> Unit
) {
    var newMaxQuantityText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Change Max Quantity") },
            text = {
                TextField(
                    value = newMaxQuantityText,
                    onValueChange = { newMaxQuantityText = it },
                    label = { Text("New Max Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        val newMaxQuantity = newMaxQuantityText.toIntOrNull() ?: 0
                        val validatedMaxQuantity = maxOf(newMaxQuantity, 0)
                        changeMaxQuantity(aggregateId, validatedMaxQuantity)
                        onDismiss()
                        refreshDetailView()
                    }
                }) {
                    Text("Change")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}