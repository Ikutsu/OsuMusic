package io.ikutsu.osumusic.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.search.data.datasource.ApiType
import io.ikutsu.osumusic.search.data.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun onTextFieldChange(text: String) {
        viewModelScope.launch {
            if (text.isBlank()) {
                onClearSearch()
            }
            _uiState.update {
                it.copy(searchText = text)
            }
        }
    }

    fun onSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_uiState.value.searchText.isNotBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        searchContent = SearchUiContent.RESULT
                    )
                }

                searchRepository.search(
                    apiType = ApiType.SAYOBOT,
                    query = _uiState.value.searchText
                ).onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchResult = data,
                            displaySearchText = if (data.isEmpty()) "No result found" else "Search result for \"${it.searchText}\""
                        )
                    }
                }
            }
        }
    }

    fun onClearSearch() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchText = "",
                    searchContent = SearchUiContent.HISTORY
                )
            }
        }
    }
}