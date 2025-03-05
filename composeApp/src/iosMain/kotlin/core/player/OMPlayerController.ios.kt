package io.ikutsu.osumusic.core.player

import io.ikutsu.osumusic.core.domain.Music
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

actual class OMPlayerController : KoinComponent {
    private val player: NativePlayerBridge by inject()

    private val _playbackState: MutableStateFlow<OMPlayerPlaybackState> =
        MutableStateFlow(OMPlayerPlaybackState())
    actual val playbackState: StateFlow<OMPlayerPlaybackState> = _playbackState.asStateFlow()

    private val _queueState: MutableStateFlow<OMPlayerQueueState> =
        MutableStateFlow(OMPlayerQueueState())
    actual val queueState: StateFlow<OMPlayerQueueState> = _queueState.asStateFlow()

    // --- Setup ---
    actual fun initializePlayer() {
        player.setOnStateChangeListener { state ->
            println(state)
            _playbackState.update {
                it.copy(playerState = state)
            }
        }
        player.setOnFinishListener {
            onPlayerEvent(OMPlayerEvent.Next)
        }
        player.setOnMusicChangeListener { isNext ->
            if (isNext) {
                onPlayerEvent(OMPlayerEvent.Next)
            } else {
                onPlayerEvent(OMPlayerEvent.Previous)
            }
        }
        player.setOnErrorListener { errorMsg ->
            _playbackState.update {
                it.copy(errorMessage = errorMsg)
            }
        }
    }

    // --- Player ---
    actual fun setPlayerItem(musics: List<Music>) {
        _queueState.update { it.copy(playerQueue = musics) }
        pause()

        setCurrentMusic(musics[0])
        play()
    }


    private fun setCurrentMusic(music: Music) {
        clearProgress()
        _queueState.update { it.copy(currentMusic = music) }

        if (music.source.endsWith(".ogg")) {
            _playbackState.update {
                it.copy(
                    playerState = OMPlayerState.Error,
                    errorMessage = "Unsupported audio format"
                )
            }
            return
        }

        player.setSource(music)
    }

    actual fun addToQueue(music: Music) {
        _queueState.update { it.copy(playerQueue = it.playerQueue + music) }
    }

    actual fun removeFromQueue(index: Int) {
        if (index in _queueState.value.playerQueue.indices) {
            _queueState.update {
                it.copy(
                    playerQueue = it.playerQueue.toMutableList().apply { removeAt(index) }
                )
            }
        }
    }

    private fun play() {
        player.play()
    }

    private fun pause() {
        player.pause()
    }

    private fun togglePlayPause() {
        if (player.isPlaying()) {
            pause()
        } else {
            play()
        }
    }

    private fun seekToPosition(progress: Float) {
        player.seekTo(progress * player.getDuration())
    }

    actual fun updateProgress() {
        _playbackState.update {
            val currentTime = player.getCurrentPosition()
            val currentTimeInSeconds = if (currentTime.isNaN()) 0.0.seconds else currentTime.seconds
            val duration = player.getDuration()
            val durationInSeconds = if (duration.isNaN()) 0.0.seconds else duration.seconds
            it.copy(
                duration = durationInSeconds,
                progress = OMPlayerPlaybackState.Progress(
                    progress = (currentTimeInSeconds / durationInSeconds).toFloat(),
                    time = currentTimeInSeconds,
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                ),
            )
        }
    }

    actual fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int
    ) {
        val playQueue = _queueState.value.playerQueue
        val currentMusicIndex = playQueue.indexOf(_queueState.value.currentMusic)
        when (event) {
            OMPlayerEvent.PlayPause -> {
                togglePlayPause()
            }

            OMPlayerEvent.Previous -> {
                if (playQueue.isNotEmpty() && currentMusicIndex > 0) {
                    setCurrentMusic(playQueue[currentMusicIndex - 1])
                }
            }

            OMPlayerEvent.Next -> {
                if (playQueue.isNotEmpty() && currentMusicIndex < playQueue.size - 1) {
                    setCurrentMusic(playQueue[currentMusicIndex + 1])
                }
            }

            OMPlayerEvent.SelectedAudioChange -> {
                if (selectedAudioIndex in playQueue.indices) {
                    setCurrentMusic(playQueue[selectedAudioIndex])
                }
            }

            is OMPlayerEvent.SeekTo -> {
                seekToPosition(event.progress)
            }
        }
    }

    private fun seekToInitialTime() {
        player.seekTo(0.0)
    }

    actual fun releasePlayer() {
        player.release()
        seekToInitialTime()
    }

    private fun clearProgress() {
        _playbackState.update {
            it.copy(
                progress = OMPlayerPlaybackState.Progress(
                    timestamp = Clock.System.now().toEpochMilliseconds()
                ),
                duration = Duration.ZERO,
            )
        }
    }
}
