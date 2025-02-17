package io.ikutsu.osumusic.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.player.OMPlayerController
import io.ikutsu.osumusic.core.player.OMPlayerEvent
import io.ikutsu.osumusic.core.player.OMPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val controller: OMPlayerController
) : ViewModel() {
    private val queueState = controller.queueState

    private val playbackState = controller.playbackState.onEach {
        updateProgressJob?.cancel()
        if (it.playerState == OMPlayerState.Playing) {
            updateProgressRequest()
        }
    }

    private val _uiState: MutableStateFlow<PlayerUiState> = MutableStateFlow(PlayerUiState())
    val uiState = combine(queueState, playbackState, _uiState) { queue, playback, uiState ->
        uiState.copy(
            currentProgress = playback.progress.progress,
            currentProgressInLong = playback.progress.time.inWholeMilliseconds,
            duration = playback.duration.inWholeMilliseconds,
            currentMusic = queue.currentMusic,
            playerQueue = queue.playerQueue,
            playerState = playback.playerState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PlayerUiState()
    )

    private var updateProgressJob: Job? = null

    fun onAction(action: PlayerAction) {
        when (action) {
            is PlayerAction.onQueueItemClick -> onQueueItemClick(action.index)
            is PlayerAction.onPlayPauseClick -> onPlayPauseClick()
            is PlayerAction.onSkipPreviousClick -> onPreviousClick()
            is PlayerAction.onSkipNextClick -> onNextClick()
            is PlayerAction.onProgressChange -> onProgressChange(action.position)
            is PlayerAction.onSeek -> onSeekTo()
            else -> Unit
        }
    }

    init {
        controller.initializePlayer()
    }

    private fun updateProgressRequest() {
        updateProgressJob = viewModelScope.launch {
            delay(50)
            controller.updateProgress()
        }
    }

    fun onPlayPauseClick() {
        controller.onPlayerEvent(OMPlayerEvent.PlayPause)
    }

    fun onPreviousClick() {
        controller.onPlayerEvent(OMPlayerEvent.Previous)
    }

    fun onNextClick() {
        controller.onPlayerEvent(OMPlayerEvent.Next)
    }

    private fun onProgressChange(progress: Float) {
        _uiState.update {
            it.copy(
                currentProgress = progress,
                currentProgressInLong = (progress * it.duration).toLong()
            )
        }
    }

    private fun onSeekTo() {
        controller.onPlayerEvent(OMPlayerEvent.SeekTo(_uiState.value.currentProgress))
    }

    private fun onQueueItemClick(index: Int) {
        controller.onPlayerEvent(OMPlayerEvent.SelectedAudioChange, index)
    }

    override fun onCleared() {
        updateProgressJob?.cancel()
        controller.releasePlayer()
    }
}