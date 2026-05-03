package uz.itschool.kitobbek.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.itschool.kitobbek.data.local.Prefs
import uz.itschool.kitobbek.data.remote.api.RetrofitClient
import uz.itschool.kitobbek.data.remote.model.request.CommentRequest
import uz.itschool.kitobbek.data.remote.model.response.BookResponse
import uz.itschool.kitobbek.data.remote.model.response.CommentResponse

data class BookDetailsUiState(
    val isLoading: Boolean = false,
    val book: BookResponse? = null,
    val comments: List<CommentResponse> = emptyList(),
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val isCommentSending: Boolean = false,
    val commentSentSuccess: Boolean = false
)

class BookDetailsViewModel(private val prefs: Prefs) : ViewModel() {
    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    fun loadBookDetails(bookId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val book = RetrofitClient.apiService.getBookById(bookId)
                val comments = RetrofitClient.apiService.getBookComments(bookId)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    book = book,
                    comments = comments,
                    isSaved = prefs.isBookSaved(bookId)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Kitob ma'lumotlarini yuklashda xatolik"
                )
            }
        }
    }

    fun toggleSaveBook() {
        val book = _uiState.value.book ?: return
        if (_uiState.value.isSaved) {
            prefs.removeBookId(book.id)
            _uiState.value = _uiState.value.copy(isSaved = false)
        } else {
            prefs.saveBookId(book.id)
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }

    fun sendComment(text: String, rating: Int) {
        val book = _uiState.value.book ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCommentSending = true, commentSentSuccess = false)
            try {
                val request = CommentRequest(
                    bookId = book.id,
                    userId = prefs.getUserId(),
                    text = text,
                    reyting = rating
                )
                RetrofitClient.apiService.createComment(request)
                _uiState.value = _uiState.value.copy(
                    isCommentSending = false,
                    commentSentSuccess = true
                )
                // Reload comments
                loadBookDetails(book.id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCommentSending = false,
                    errorMessage = "Sharh qoldirishda xatolik: ${e.message}"
                )
            }
        }
    }

    fun resetCommentStatus() {
        _uiState.value = _uiState.value.copy(commentSentSuccess = false)
    }
}
