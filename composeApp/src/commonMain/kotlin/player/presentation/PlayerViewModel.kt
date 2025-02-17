package io.ikutsu.osumusic.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.core.player.OMPlayerController
import io.ikutsu.osumusic.core.player.OMPlayerEvent
import io.ikutsu.osumusic.core.player.OMPlayerListener
import io.ikutsu.osumusic.core.player.OMPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val controller: OMPlayerController
): ViewModel() {

    private val _uiState: MutableStateFlow<PlayerUiState> = MutableStateFlow(PlayerUiState())
    val uiState = _uiState
        .stateIn(
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
        registerListener()
    }

    private fun registerListener() {
        controller.registerListener(
            object : OMPlayerListener {
                override fun onError(message: String) {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                isError = true,
                                errorMessage = message
                            )
                        }
                        delay(3000)
                        _uiState.update {
                            it.copy(isError = false)
                        }
                    }
                }

                override fun onProgress(progress: Long) {
                    if (_uiState.value.playerState == OMPlayerState.Playing || _uiState.value.playerState == OMPlayerState.Buffering) {
                        updateProgressJob = viewModelScope.launch {
                            _uiState.update {
                                it.copy(
                                    currentProgressInLong = progress,
                                    currentProgress = if (progress == 0L) 0f else progress.toFloat() / it.duration.toFloat()
                                )
                            }
                        }
                    }
                }

                override fun totalDuration(duration: Long) {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(duration = duration)
                        }
                    }
                }

                override fun currentPlayerState(state: OMPlayerState) {
                    viewModelScope.launch {
                        println("currentPlayerState: $state")
                        _uiState.update {
                            it.copy(playerState = state)
                        }
                        updateProgressJob?.cancel()
                    }
                }

                override fun currentMusic(music: Music?) {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(currentMusic = music)
                        }
                    }
                }

                override fun onQueueChanged(queue: List<Music>) {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(playerQueue = queue)
                        }
                    }
                }
            }
        )
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
        controller.release()
    }
}