package uz.itschool.kitobbek.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSeeAllClick: (String) -> Unit,
    onBookClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ProfileTopBar(
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            LoadingView()
        } else if (uiState.errorMessage != null) {
            ErrorView(message = uiState.errorMessage!!)
        } else {
            ProfileContent(
                modifier = Modifier.padding(innerPadding),
                uiState = uiState,
                onSeeAllClick = onSeeAllClick,
                onBookClick = onBookClick
            )
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    onSeeAllClick: (String) -> Unit,
    onBookClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            ProfileHeader(
                userName = uiState.userName,
                email = uiState.email
            )
        }

        item {
            StatsCard(
                readingCount = uiState.readingBooks.size,
                readCount = uiState.readBooks.size,
                savedCount = uiState.savedBooks.size
            )
        }

        // O'qilayotgan kitoblar section
        if (uiState.readingBooks.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "O'qilayotgan kitoblar",
                    onSeeAllClick = { onSeeAllClick("READING") }
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(uiState.readingBooks) { book ->
                        ReadingBookCard(book = book, onBookClick = onBookClick)
                    }
                }
            }
        }

        // O'qilgan kitoblar section
        if (uiState.readBooks.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "O'qilgan kitoblar",
                    onSeeAllClick = { onSeeAllClick("READ") }
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(uiState.readBooks) { book ->
                        SmallBookCard(book = book, onBookClick = onBookClick)
                    }
                }
            }
        }

        // Saqlangan kitoblar section
        if (uiState.savedBooks.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Saqlangan kitoblar",
                    onSeeAllClick = { onSeeAllClick("SAVED") }
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(uiState.savedBooks) { book ->
                        SmallBookCard(book = book, onBookClick = onBookClick)
                    }
                }
            }
        }
    }
}
