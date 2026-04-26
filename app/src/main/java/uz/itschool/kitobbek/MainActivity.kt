package uz.itschool.kitobbek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uz.itschool.kitobbek.ui.screen.CategoryBooksScreen
import uz.itschool.kitobbek.ui.screen.ProfileScreen
import uz.itschool.kitobbek.ui.screen.ProfileViewModel
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

    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") {
            ProfileScreen(
                viewModel = profileViewModel,
                onBackClick = { /* Handle back */ },
                onSettingsClick = { /* Handle settings */ },
                onSeeAllClick = { status ->
                    navController.navigate("category/$status")
                },
                onBookClick = { bookId ->
                    // Handle book click
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
                onBookClick = { bookId ->
                    // Handle book click
                }
            )
        }
    }
}
