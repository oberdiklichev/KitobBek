package uz.itschool.kitobbek.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.itschool.kitobbek.data.local.Prefs
import uz.itschool.kitobbek.data.remote.api.RetrofitClient
import uz.itschool.kitobbek.data.remote.model.response.BookResponse

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "Aliyev Ali",
    val email: String = "ali@gmail.com",
    val readingBooks: List<BookResponse> = emptyList(),
    val readBooks: List<BookResponse> = emptyList(),
    val savedBooks: List<BookResponse> = emptyList(),
    val errorMessage: String? = null
)

class ProfileViewModel(private val prefs: Prefs) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val allBooks = RetrofitClient.apiService.getAllBooks()
                val savedIds = prefs.getSavedBookIds()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    readingBooks = emptyList(),
                    readBooks = emptyList(),
                    savedBooks = allBooks.filter { it.id.toString() in savedIds }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Ma'lumotlarni yuklashda xatolik: ${e.message}"
                )
            }
        }
    }
}
