package io.ikutsu.osumusic.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.data.getBeatmapBackgroundUrl
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.player.player.OMPlayerController
import io.ikutsu.osumusic.player.player.OMPlayerEvent
import io.ikutsu.osumusic.search.data.datasource.ApiType
import io.ikutsu.osumusic.search.data.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val playerController: OMPlayerController
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var queryJob: Job? = null

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
        cancelQueryJob()
        queryJob = viewModelScope.launch(Dispatchers.IO) {
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
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchResult = emptyList(),
                            displaySearchText = "Error: ${error.message}"
                        )
                    }
                }
            }
        }
    }

    fun onClearSearch() {
        viewModelScope.launch {
            cancelQueryJob()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    searchText = "",
                    searchContent = SearchUiContent.HISTORY
                )
            }
        }
    }

    private fun cancelQueryJob() {
        queryJob?.cancel()
        queryJob = null
    }

    fun onSearchItemClick(
        beatmapState: DiffBeatmapState
    ) {
        viewModelScope.launch {
            playerController.addPlayerItem(
                listOf(
                    Music(
                        title = beatmapState.title,
                        artist = beatmapState.artist,
                        creator = beatmapState.creator,
                        diff = beatmapState.diff.first(),
                        coverUrl = beatmapState.coverUrl,
                        backgroundUrl = getBeatmapBackgroundUrl(beatmapState.beatmapId),
                        source = beatmapState.audioUrl
                    )
                )
            )
            playerController.onPlayerEvent(
                OMPlayerEvent.PlayPause
            )
        }
    }
}