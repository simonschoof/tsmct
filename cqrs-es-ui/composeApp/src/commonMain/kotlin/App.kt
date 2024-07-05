import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

object MainDestinations {
    const val INVENTORY_LIST_ROUTE = "inventoryList"
    const val DETAIL_PAGE_ROUTE = "detailPage"
    const val ITEM_ID_KEY = "itemId"
}

@Composable
@Preview
fun App(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = MainDestinations.INVENTORY_LIST_ROUTE) {
        composable(MainDestinations.INVENTORY_LIST_ROUTE) {
            InventoryList(navController = navController)
        }
        composable("${MainDestinations.DETAIL_PAGE_ROUTE}/{${MainDestinations.ITEM_ID_KEY}}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString(MainDestinations.ITEM_ID_KEY)
            itemId?.let {
                DetailPage(itemId, navController)
            }
        }
    }
}
