package uz.itschool.kitobbek.ui.theme.menucomponents
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import uz.itschool.kitobbek.R
// ─── Navigation Destinations ────────────────────────────────────────────────

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int          // ← point each to your drawable resource
) {
    object Home       : BottomNavItem("home",       "Bosh sahifa", R.drawable.book_ico)
    object Search     : BottomNavItem("search",     "Qidirish",    R.drawable.search_ico)
    object Write      : BottomNavItem("write",      "Yozish",      R.drawable.feather_ico)
    object Bookmarks  : BottomNavItem("bookmarks",  "Saqlangan",   R.drawable.bookmark_ico)
    object Language   : BottomNavItem("language",   "Til",         R.drawable.internet_ico)
}

private val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Search,
    BottomNavItem.Write,
    BottomNavItem.Bookmarks,
    BottomNavItem.Language
)

// ─── Colours (match the screenshot) ─────────────────────────────────────────

private val NavActiveColor   = Color(0xFF1A237E)   // dark navy
private val NavInactiveColor = Color(0xFFB0BEC5)   // light grey
private val NavBackground    = Color.White

// ─── Composable ─────────────────────────────────────────────────────────────

/**
 * Reusable bottom navigation bar.
 *
 * Usage – paste inside every Scaffold:
 *
 *   Scaffold(
 *       bottomBar = { BottomNavBar(navController = navController) }
 *   ) { ... }
 */

@Composable
fun BottomNavBar(navController: NavController) {

    NavigationBar(containerColor = Color.White) {
        bottomNavItems.forEach { item ->

            NavigationBarItem(
                selected = navController.currentDestination?.route == item.route,
                onClick  = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        painter            = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier           = Modifier.size(24.dp)
                    )
                },
                label  = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Color(0xFF0D1B4B),
                    unselectedIconColor = Color(0xFFB0BEC5),
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}