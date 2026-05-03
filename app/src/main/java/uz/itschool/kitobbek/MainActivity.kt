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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import uz.itschool.kitobbek.ui.screens.details.BookDetailsScreen
import uz.itschool.kitobbek.ui.screens.details.BookDetailsViewModel
import uz.itschool.kitobbek.ui.screens.category.CategoryBooksScreen
import uz.itschool.kitobbek.ui.screens.category.CategoryViewModel
import uz.itschool.kitobbek.ui.screens.pdf.PdfReaderScreen
import uz.itschool.kitobbek.ui.screens.articles.ArticlesScreen
import uz.itschool.kitobbek.ui.theme.KitobBekTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    
    val profileViewModel: ProfileViewModel = remember { ProfileViewModel(prefs) }
    val uiState by profileViewModel.uiState.collectAsState()
    
    val bookDetailsViewModel: BookDetailsViewModel = remember { BookDetailsViewModel(prefs) }
    val categoryViewModel: CategoryViewModel = remember { CategoryViewModel(prefs) }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentStatus = navBackStackEntry?.arguments?.getString("status")

    val startDestination = if (prefs.isRegistered()) "home" else "welcome"

    val mainRoutes = listOf("home", "search", "write", "language")
    val isMainRoute = currentRoute in mainRoutes || (currentRoute == "category/{status}" && currentStatus == "SAVED")

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isMainRoute,
        drawerContent = {
            AppDrawer(
                userName = prefs.getUserName(),
                userEmail = prefs.getUserEmail(),
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
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBookClick = { bookId ->
                        navController.navigate("details/$bookId")
                    }
                )
            }

            composable("profile") {
                LaunchedEffect(Unit) {
                    profileViewModel.loadProfileData()
                }
                ProfileScreen(
                    viewModel = profileViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSettingsClick = { /* Sozlamalar */ },
                    onSeeAllClick = { status ->
                        navController.navigate("category/$status")
                    },
                    onBookClick = { bookId ->
                        navController.navigate("details/$bookId")
                    }
                )
            }

            composable(
                route = "category/{status}",
                arguments = listOf(navArgument("status") { type = NavType.StringType })
            ) { backStackEntry ->
                val status = backStackEntry.arguments?.getString("status") ?: ""
                val categoryUiState by categoryViewModel.uiState.collectAsState()

                val profileStatuses = listOf("READING", "READ", "SAVED")
                
                LaunchedEffect(status, uiState) {
                    if (status in profileStatuses) {
                        categoryViewModel.loadBooksByStatus(
                            status,
                            uiState.readingBooks,
                            uiState.readBooks,
                            uiState.savedBooks
                        )
                    } else {
                        categoryViewModel.loadBooksByCategory(status)
                    }
                }

                val isSaved = status == "SAVED"

                CategoryBooksScreen(
                    categoryTitle = categoryUiState.title,
                    books = categoryUiState.books,
                    isLoading = categoryUiState.isLoading,
                    onBackClick = { 
                        if (isSaved) scope.launch { drawerState.open() }
                        else navController.popBackStack() 
                    },
                    onBookClick = { bookId ->
                        navController.navigate("details/$bookId")
                    },
                    isMainDestination = isSaved,
                    bottomBar = {
                        if (isSaved) BottomNavBar(navController = navController)
                    }
                )
            }

            composable(
                route = "details/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.IntType })
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
                BookDetailsScreen(
                    bookId = bookId,
                    viewModel = bookDetailsViewModel,
                    onBackClick = { navController.popBackStack() },
                    onReadClick = { book ->
                        val encodedName = URLEncoder.encode(book.name, StandardCharsets.UTF_8.toString())
                        val encodedUrl = URLEncoder.encode(book.file, StandardCharsets.UTF_8.toString())
                        navController.navigate("pdf_reader/${book.id}/$encodedName/$encodedUrl")
                    }
                )
            }

            composable(
                route = "pdf_reader/{bookId}/{bookName}/{bookFile}",
                arguments = listOf(
                    navArgument("bookId") { type = NavType.IntType },
                    navArgument("bookName") { type = NavType.StringType },
                    navArgument("bookFile") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
                val bookName = URLDecoder.decode(
                    backStackEntry.arguments?.getString("bookName") ?: "",
                    StandardCharsets.UTF_8.toString()
                )
                val bookFile = URLDecoder.decode(
                    backStackEntry.arguments?.getString("bookFile") ?: "",
                    StandardCharsets.UTF_8.toString()
                )

                PdfReaderScreen(
                    bookId = bookId,
                    bookName = bookName,
                    bookFile = bookFile,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("search") {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White,
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
                        SearchScreen(
                            onBookClick = { bookId ->
                                navController.navigate("details/$bookId")
                            }
                        )
                    }
                }
            }
            composable("write") {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White,
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ArticlesScreen(
                            onMenuClick = { scope.launch { drawerState.open() } },
                            onProfileClick = { navController.navigate("profile") }
                        )
                    }
                }
            }
            composable("language")   { /* LanguageScreen() */ }
        }
    }
}
