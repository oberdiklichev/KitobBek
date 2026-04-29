package uz.itschool.kitobbek.homepage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.itschool.kitobbek.data.remote.api.RetrofitClient
import uz.itschool.kitobbek.data.remote.model.response.BookResponse
import uz.itschool.kitobbek.data.remote.model.response.CategoryResponse

class HomeViewModel : ViewModel() {

    var allBooks by mutableStateOf(listOf<BookResponse>())
    var categories by mutableStateOf(listOf<CategoryResponse>())
    var mainBook by mutableStateOf(BookResponse())
    var isLoading by mutableStateOf(true)
    var error by mutableStateOf("")

    init {
        viewModelScope.launch {
            try {
                allBooks = RetrofitClient.apiService.getAllBooks()
                categories = RetrofitClient.apiService.getAllCategories()
                mainBook = RetrofitClient.apiService.getMainBook()
            } catch (e: Exception) {
                error = "Xatolik: ${e.message}"
            }
            isLoading = false
        }
    }
}