package uz.itschool.kitobbek.ui.screens.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itschool.kitobbek.data.remote.model.response.BookResponse
import uz.itschool.kitobbek.ui.components.BottomNavBar
import uz.itschool.kitobbek.ui.components.TopBar
import uz.itschool.kitobbek.ui.screens.profile.GridBookCard
import uz.itschool.kitobbek.ui.screens.profile.PrimaryDarkBlue
import uz.itschool.kitobbek.ui.screens.profile.SecondaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBooksScreen(
    categoryTitle: String,
    books: List<BookResponse>,
    onBackClick: () -> Unit,
    onBookClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit = {},
    isMainDestination: Boolean = false
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = categoryTitle,
                onMenuClick = onBackClick,
                menuIcon = if (isMainDestination) Icons.Default.Menu else Icons.AutoMirrored.Filled.ArrowBack
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        if (books.isEmpty()) {
            val emptyMessage = when (categoryTitle) {
                "Saqlangan kitoblar" -> "Saqlangan kitoblar yo'q"
                "O'qilayotgan kitoblar" -> "Hozircha o'qilayotgan kitoblar yo'q"
                "O'qilgan kitoblar" -> "Hozircha o'qilgan kitoblar yo'q"
                else -> "Kitoblar topilmadi"
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = emptyMessage, color = SecondaryText)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(books) { book ->
                    GridBookCard(book = book, onBookClick = onBookClick)
                }
            }
        }
    }
}
