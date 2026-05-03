package uz.itschool.kitobbek.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.itschool.kitobbek.data.local.Prefs
import uz.itschool.kitobbek.data.remote.api.RetrofitClient
import uz.itschool.kitobbek.data.remote.model.response.BookResponse

data class CategoryUiState(
    val isLoading: Boolean = false,
    val books: List<BookResponse> = emptyList(),
    val title: String = "",
    val errorMessage: String? = null
)

class CategoryViewModel(private val prefs: Prefs) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun loadBooksByStatus(status: String, readingBooks: List<BookResponse>, readBooks: List<BookResponse>, savedBooks: List<BookResponse>) {
        val (books, title) = when (status) {
            "READING" -> readingBooks to "O'qilayotgan kitoblar"
            "READ" -> readBooks to "O'qilgan kitoblar"
            "SAVED" -> savedBooks to "Saqlangan kitoblar"
            else -> emptyList<BookResponse>() to "Kitoblar"
        }
        _uiState.value = CategoryUiState(books = books, title = title, isLoading = false)
    }

    fun loadBooksByCategory(categoryName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, title = categoryName)
            try {
                // For "Romanlar" and "Darsliklar" we can use the search or category API
                // Assuming "category" API works for these names
                val books = RetrofitClient.apiService.getBooksByCategory(categoryName)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    books = books
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Kategoriyadagi kitoblarni yuklashda xatolik: ${e.message}"
                )
            }
        }
    }
}
