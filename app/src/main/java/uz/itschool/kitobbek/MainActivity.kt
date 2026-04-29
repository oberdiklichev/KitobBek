package uz.itschool.kitobbek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uz.itschool.kitobbek.homepage.HomeScreen
import uz.itschool.kitobbek.ui.theme.KitobBekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitobBekTheme {                                          // 1. Theme is outermost
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(                                         // 2. NavHost is inside Scaffold
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)   // 3. innerPadding applied here
                    ) {
                        composable("home")      { HomeScreen(navController) }
                        composable("search")    { /* SearchScreen() */ }
                        composable("write")     { /* WriteScreen() */ }
                        composable("bookmarks") { /* BookmarksScreen() */ }
                        composable("language")  { /* LanguageScreen() */ }
                    }
                }
            }
        }
    }
}
