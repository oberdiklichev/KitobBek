package uz.itschool.kitobbek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uz.itschool.kitobbek.ui.screen.SearchScreen
import uz.itschool.kitobbek.ui.screens.home.HomeScreen
import uz.itschool.kitobbek.ui.screens.profile.ProfileScreen
import uz.itschool.kitobbek.ui.screens.profile.ProfileViewModel
import uz.itschool.kitobbek.ui.screens.profile.ProfileUiState
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
    val profileViewModel: ProfileViewModel = viewModel()
    val uiState by profileViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
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
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
