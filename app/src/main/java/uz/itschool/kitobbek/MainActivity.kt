package uz.itschool.kitobbek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import uz.itschool.kitobbek.data.local.Prefs
import uz.itschool.kitobbek.ui.screens.auth.LoginScreen
import uz.itschool.kitobbek.ui.screens.auth.RegisterScreen
import uz.itschool.kitobbek.ui.screens.auth.WelcomeScreen
import uz.itschool.kitobbek.ui.screens.search.SearchScreen
import uz.itschool.kitobbek.ui.screens.home.HomeScreen
import uz.itschool.kitobbek.ui.components.BottomNavBar
import uz.itschool.kitobbek.ui.components.TopBar
import uz.itschool.kitobbek.ui.components.AppDrawer
import uz.itschool.kitobbek.ui.screens.profile.ProfileScreen
import uz.itschool.kitobbek.ui.screens.profile.ProfileViewModel
import uz.itschool.kitobbek.ui.screens.category.CategoryBooksScreen
import uz.itschool.kitobbek.ui.theme.KitobBekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitobBekTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }
    
    val profileViewModel: ProfileViewModel = viewModel()
    val uiState by profileViewModel.uiState.collectAsState()
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val startDestination = if (prefs.isRegistered()) "home" else "welcome"

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute != "welcome" && currentRoute != "login" && currentRoute != "register",
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onLoginClick = { navController.navigate("login") },
                    onRegisterClick = { navController.navigate("register") }
                )
            }
            composable("login") {
                LoginScreen(
                    onBackClick = { navController.popBackStack() },
                    onRegisterClick = { navController.navigate("register") },
                    onLoginSuccess = { 
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onBackClick = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }

            composable("profile") {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSettingsClick = { /* Sozlamalar */ },
                    onSeeAllClick = { status ->
                        navController.navigate("category/$status")
                    },
                    onBookClick = { _ ->
                        // Kitob ustiga bosilganda
                    }
                )
            }

            composable(
                route = "category/{status}",
                arguments = listOf(navArgument("status") { type = NavType.StringType })
            ) { backStackEntry ->
                val status = backStackEntry.arguments?.getString("status") ?: ""
                val books = when (status) {
                    "READING" -> uiState.readingBooks
                    "READ" -> uiState.readBooks
                    "SAVED" -> uiState.savedBooks
                    else -> emptyList()
                }
                val title = when (status) {
                    "READING" -> "O'qilayotgan kitoblar"
                    "READ" -> "O'qilgan kitoblar"
                    "SAVED" -> "Saqlangan kitoblar"
                    else -> "Kitoblar"
                }
                CategoryBooksScreen(
                    categoryTitle = title,
                    books = books,
                    onBackClick = { navController.popBackStack() },
                    onBookClick = { _ ->
                        // Kitob ustiga bosilganda
                    }
                )
            }

            composable("search") {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar(
                            title = "Qidirish",
                            onMenuClick = { scope.launch { drawerState.open() } },
                            onProfileClick = { navController.navigate("profile") }
                        )
                    },
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SearchScreen()
                    }
                }
            }
            composable("write")      { /* WriteScreen() */ }
            composable("bookmarks")  { /* BookmarksScreen() */ }
            composable("language")   { /* LanguageScreen() */ }
        }
    }
}
