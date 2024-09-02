package io.ikutsu.osumusic.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.player.player.OMPlayerController
import io.ikutsu.osumusic.player.player.OMPlayerEvent
import io.ikutsu.osumusic.player.player.OMPlayerListener
import io.ikutsu.osumusic.player.player.OMPlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PlayerViewModel(
    private val controller: OMPlayerController
): ViewModel() {

    private val _uiState: MutableStateFlow<PlayerUiState> = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        registerListener()
    }

    private fun registerListener() {
        controller.registerListener(
            object : OMPlayerListener {
                override fun onError() {
                }

                override fun onProgress(progress: Long) {

                }

                override fun totalDuration(duration: Long) {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(duration = duration)
                        }
                    }
                }

                override fun currentPlayerState(state: OMPlayerState) {
                    viewModelScope.launch(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(playerState = state)
                        }
                        if (state == OMPlayerState.Playing) {
                            while (true) { // TODO: Need to debounce
                                delay(1.seconds)
                                val position = controller.getCurrentPosition()
                                _uiState.update {
                                    it.copy(
                                        currentProgressInLong = position,
                                        currentProgress = if (position == 0L) 0f else position.toFloat() / it.duration.toFloat()
                                    )
                                }
                            }
                        }
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
}