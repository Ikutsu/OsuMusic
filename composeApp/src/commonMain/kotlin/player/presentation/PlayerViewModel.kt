package io.ikutsu.osumusic.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.player.player.OMPlayerController
import io.ikutsu.osumusic.player.player.OMPlayerEvent
import io.ikutsu.osumusic.player.player.OMPlayerListener
import io.ikutsu.osumusic.player.player.OMPlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val controller: OMPlayerController
): ViewModel() {

    private val _uiState: MutableStateFlow<PlayerUiState> = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    private var updateProgressJob: Job? = null

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

    fun onProgressChange(progress: Float) {
        _uiState.update {
            it.copy(
                currentProgress = progress,
                currentProgressInLong = (progress * it.duration).toLong()
            )
        }
    }

    fun onSeekTo() {
        controller.onPlayerEvent(OMPlayerEvent.SeekTo(_uiState.value.currentProgress))
    }

    override fun onCleared() {
        updateProgressJob?.cancel()
        controller.release()
    }
}