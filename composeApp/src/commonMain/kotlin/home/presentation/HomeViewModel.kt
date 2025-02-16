package io.ikutsu.osumusic.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.data.model.toBeatmapMetadata
import io.ikutsu.osumusic.core.data.model.toMusic
import io.ikutsu.osumusic.core.data.repository.PlayHistoryRepository
import io.ikutsu.osumusic.core.domain.BeatmapMetadata
import io.ikutsu.osumusic.player.player.OMPlayerController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val playHistoryRepository: PlayHistoryRepository,
    private val playerController: OMPlayerController
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState
        .onStart { fetchPlayHistory() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeUiState()
        )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnPlayHistoryClick -> playBeatmapHistory(action.beatmapMetadata)
            else -> Unit
        }
    }

    private fun fetchPlayHistory() {
        viewModelScope.launch {
            playHistoryRepository.getPlayHistory().collect { data ->
                _uiState.update { state ->
                    state.copy(
                        recentPlayedList = data
                            .sortedByDescending { it.addedAt.epochSeconds }
                            .map { it.toBeatmapMetadata() }
                    )
                }
            }
        }
    }

    private fun playBeatmapHistory(
        beatmapMetadata: BeatmapMetadata
    ) {
        viewModelScope.launch {
            playHistoryRepository.savePlayHistory(beatmapMetadata)
            playerController.setPlayerItem(
                playHistoryRepository
                    .getPlayHistory()
                    .first()
                    .sortedByDescending { it.addedAt }
                    .map { it.toMusic() }
            )
        }
    }
}