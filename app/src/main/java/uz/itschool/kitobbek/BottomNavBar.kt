package uz.itschool.kitobbek

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry?.destination?.route

    NavigationBar(
        containerColor = NavBackground,
        tonalElevation = 8.dp,
        modifier        = Modifier.height(64.dp)
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick  = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Pop up to start so back-stack doesn't grow infinitely
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter           = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        tint               = if (selected) NavActiveColor else NavInactiveColor,
                        modifier           = Modifier.size(24.dp)
                    )
                },
                label = null,                          // no labels → icon-only bar
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = NavActiveColor,
                    unselectedIconColor = NavInactiveColor,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}