package io.ikutsu.osumusic.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.domain.BeatmapMetadata
import io.ikutsu.osumusic.core.domain.toMusic
import io.ikutsu.osumusic.core.player.OMPlayerController
import io.ikutsu.osumusic.search.data.local.toBeatmapMetadata
import io.ikutsu.osumusic.search.data.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val playerController: OMPlayerController
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState
        .onStart { fetchSearchHistory() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SearchUiState()
        )

    private var queryJob: Job? = null

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnSearchClick -> searchBeatmaps()
            is SearchAction.OnSearchQueryClear -> clearSearchQuery()
            is SearchAction.OnSearchQueryChange -> onSearchQueryChange(action.query)
            is SearchAction.OnSearchResultClick -> playSelectedBeatmap(action.beatmapMetadata)
            is SearchAction.OnSearchResultSwipe -> addBeatmapToQueue(action.beatmapMetadata)
            else -> Unit
        }
    }

    private fun fetchSearchHistory() {
        viewModelScope.launch {
            searchRepository.getSearchHistory().collect { data ->
                _uiState.update { state ->
                    state.copy(
                        searchHistory = data
                            .sortedByDescending { it.addedAt.epochSeconds }
                            .map { it.toBeatmapMetadata() }
                    )
                }
            }
        }
    }

    private fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                clearSearchQuery()
            }
            _uiState.update {
                it.copy(searchQuery = query)
            }
        }
    }

    private fun searchBeatmaps() {
        cancelQueryJob()
        queryJob = viewModelScope.launch(Dispatchers.IO) {
            if (_uiState.value.searchQuery.isNotBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        searchContent = SearchUiContent.RESULT
                    )
                }

                searchRepository.search(
                    query = _uiState.value.searchQuery
                ).onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchResult = data,
                            displayContentTitle = if (data.isEmpty()) "No result found" else "Search result for \"${it.searchQuery}\""
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchResult = emptyList(),
                            displayContentTitle = "Error: ${error.message}"
                        )
                    }
                }
            }
        }
    }

    private fun clearSearchQuery() {
        viewModelScope.launch {
            cancelQueryJob()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    searchQuery = "",
                    searchContent = SearchUiContent.HISTORY
                )
            }
        }
    }

    private fun cancelQueryJob() {
        queryJob?.cancel()
        queryJob = null
    }

    private fun playSelectedBeatmap(
        beatmapMetadata: BeatmapMetadata
    ) {
        viewModelScope.launch {
            searchRepository.saveSearchHistory(beatmapMetadata)
            playerController.setPlayerItem(listOf(beatmapMetadata.toMusic()))
        }
    }

    private fun addBeatmapToQueue(
        beatmapMetadata: BeatmapMetadata
    ) {
        viewModelScope.launch {
            searchRepository.saveSearchHistory(beatmapMetadata)
            playerController.addToQueue(beatmapMetadata.toMusic())
        }
    }
}