package io.ikutsu.osumusic.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.data.Constants
import io.ikutsu.osumusic.core.data.Osu
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.player.player.OMPlayerController
import io.ikutsu.osumusic.player.player.OMPlayerEvent
import io.ikutsu.osumusic.search.data.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalSettingsApi::class)
class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val playerController: OMPlayerController,
    private val settingStorage: FlowSettings
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var queryJob: Job? = null

    init {
        fetchSearchHistory()
        getSettings()
    }

    private fun fetchSearchHistory() {
        viewModelScope.launch {
            searchRepository.getSearchHistory().collect { data ->
                _uiState.update { state ->
                    state.copy(
                        searchHistory = data.sortedByDescending { it.addedAt.epochSeconds }.map {
                            DiffBeatmapState(
                                beatmapId = it.beatmapId,
                                title = it.title,
                                titleUnicode = it.titleUnicode,
                                artist = it.artist,
                                artistUnicode = it.artistUnicode,
                                creator = it.creator,
                                diff = it.difficulty.toList(),
                                coverUrl = it.coverUrl,
                                audioUrl = it.audioUrl
                            )
                        }
                    )
                }
            }
        }
    }

    private fun getSettings() {
        viewModelScope.launch {
            settingStorage.getBooleanOrNullFlow(Constants.Setting.SHOW_IN_ORIGINAL_LANG).collect { showInOriginalLang ->
                _uiState.update {
                    it.copy(
                        isUnicode = showInOriginalLang ?: false
                    )
                }
            }
        }
    }

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
                    apiType = BeatmapSource.valueOf(settingStorage.getStringOrNull(Constants.Setting.BEATMAP_SOURCE) ?: BeatmapSource.SAYOBOT.name),
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
            searchRepository.saveSearchHistory(beatmapState)
            playerController.setPlayerItem(
                listOf(
                    Music(
                        title = beatmapState.title,
                        artist = beatmapState.artist,
                        creator = beatmapState.creator,
                        diff = beatmapState.diff.first(),
                        coverUrl = beatmapState.coverUrl,
                        backgroundUrl = Osu.getBeatmapBackgroundUrl(beatmapState.beatmapId),
                        source = beatmapState.audioUrl
                    )
                )
            )
            playerController.onPlayerEvent(
                OMPlayerEvent.PlayPause
            )
        }
    }

    fun onSwipeRelease(
        beatmapState: DiffBeatmapState
    ) {
        viewModelScope.launch {
            searchRepository.saveSearchHistory(beatmapState)
            playerController.addToQueue(
                Music(
                    title = beatmapState.title,
                    artist = beatmapState.artist,
                    creator = beatmapState.creator,
                    diff = beatmapState.diff.first(),
                    coverUrl = beatmapState.coverUrl,
                    backgroundUrl = Osu.getBeatmapBackgroundUrl(beatmapState.beatmapId),
                    source = beatmapState.audioUrl
                )
            )
        }
    }
}