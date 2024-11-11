package io.ikutsu.osumusic.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.data.Osu
import io.ikutsu.osumusic.core.data.repository.PlayHistoryRepository
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.player.player.OMPlayerController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val playHistoryRepository: PlayHistoryRepository,
    private val playerController: OMPlayerController
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchPlayHistory()
    }

    private fun fetchPlayHistory() {
        viewModelScope.launch {
            playHistoryRepository.getPlayHistory().collect { data ->
                _uiState.update { state ->
                    state.copy(
                        recentPlayedList = data.sortedByDescending { it.addedAt.epochSeconds }.map {
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

    fun onPlayHistoryClicked(
        beatmapState: DiffBeatmapState
    ) {
        viewModelScope.launch {
            playHistoryRepository.savePlayHistory(beatmapState)
            playerController.setPlayerItem(
                playHistoryRepository.getPlayHistory().first().sortedByDescending { it.addedAt }.map {
                    Music(
                        title = it.title,
                        artist = it.artist,
                        creator = it.creator,
                        diff = it.difficulty.toList().first(),
                        coverUrl = it.coverUrl,
                        backgroundUrl = Osu.getBeatmapBackgroundUrl(it.beatmapId),
                        source = it.audioUrl
                    )
                }
            )
        }
    }
}