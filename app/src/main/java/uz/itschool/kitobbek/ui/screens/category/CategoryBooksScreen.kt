package uz.itschool.kitobbek.ui.screens.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itschool.kitobbek.data.remote.model.response.BookResponse
import uz.itschool.kitobbek.ui.screens.profile.EmptyView
import uz.itschool.kitobbek.ui.screens.profile.GridBookCard
import uz.itschool.kitobbek.ui.screens.profile.PrimaryDarkBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBooksScreen(
    categoryTitle: String,
    books: List<BookResponse>,
    onBackClick: () -> Unit,
    onBookClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = categoryTitle,
                        color = PrimaryDarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryDarkBlue
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (books.isEmpty()) {
            EmptyView()
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
