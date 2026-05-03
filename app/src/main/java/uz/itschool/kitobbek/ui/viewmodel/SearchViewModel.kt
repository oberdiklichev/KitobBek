package uz.itschool.kitobbek.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.itschool.kitobbek.data.remote.api.RetrofitClient
import uz.itschool.kitobbek.data.remote.model.response.BookResponse

class SearchViewModel : ViewModel() {
    private val _searchResult = MutableStateFlow<List<BookResponse>>(emptyList())
    val searchResult: StateFlow<List<BookResponse>> = _searchResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun searchBooks(query: String) {
        if (query.isBlank()) {
            _searchResult.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.apiService.searchBookByName(query)
                _searchResult.value = response
            } catch (e: Exception) {
                _searchResult.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
