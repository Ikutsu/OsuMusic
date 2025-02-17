package io.ikutsu.osumusic.core.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_IDLE
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.service.OMPlayerService
import io.ikutsu.osumusic.setting.data.AppearanceSettings
import io.ikutsu.osumusic.setting.data.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

actual class OMPlayerController(
    context: Context,
    private val settingRepository: SettingRepository
) {

    private var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    private val _queueState = MutableStateFlow(OMPlayerQueueState())
    actual val queueState: StateFlow<OMPlayerQueueState> = _queueState.asStateFlow()

    private val _playbackState = MutableStateFlow(OMPlayerPlaybackState())
    actual val playbackState: StateFlow<OMPlayerPlaybackState> = _playbackState.asStateFlow()

    private val appearanceSettings: MutableStateFlow<AppearanceSettings> =
        MutableStateFlow(AppearanceSettings(false))

    private val controllerJob = SupervisorJob()
    private val controllerScope = CoroutineScope(Dispatchers.Main + controllerJob)

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, OMPlayerService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerScope.launch {
            settingRepository.getAppearanceSettings().collect {
                appearanceSettings.value = it
            }
        }
    }

    actual fun initializePlayer() {
        controllerFuture.addListener({
            controller?.addListener(object : Player.Listener {

                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    println(events)
                    with(player) {
                        _playbackState.update {
                            it.copy(
                                playerState = when (playbackState) {
                                    STATE_IDLE -> return
                                    STATE_BUFFERING -> OMPlayerState.Buffering
                                    STATE_ENDED -> OMPlayerState.Stopped
                                    else -> if (isPlaying) OMPlayerState.Playing else OMPlayerState.Paused
                                },
                                duration = if (duration == C.TIME_UNSET) (0).milliseconds else duration.milliseconds
                            )
                        }
                        _queueState.update {
                            it.copy(
                                currentMusic = _queueState.value.playerQueue.getOrNull(currentMediaItemIndex)
                            )
                        }
                        updateProgress()
                    }
                }

                override fun onPlayerErrorChanged(error: PlaybackException?) {
                    super.onPlayerErrorChanged(error)
                    val errorMsg = when (error?.errorCode) {
                        null -> return
                        PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> "Source unavailable"
                        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "Network connection failed"
                        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> "Network connection timeout"
                        else -> "Player error"
                    }
                    _playbackState.update {
                        it.copy(
                            playerState = OMPlayerState.Error,
                            errorMessage = errorMsg
                        )
                    }
                }


            })
        }, MoreExecutors.directExecutor())
    }

    actual fun setPlayerItem(
        musics: List<Music>
    ) {
        _queueState.update { it.copy(playerQueue = musics) }
        val mediaItems = musics.map { it.toMediaItem(appearanceSettings.value.showInOriginalLang) }
        controller?.setMediaItems(mediaItems)

        controller?.playWhenReady = true
        controller?.prepare()
        updateNotificationMetadata()
    }

    actual fun addToQueue(music: Music) {
        val mediaItem = music.toMediaItem(appearanceSettings.value.showInOriginalLang)

        _queueState.update { it.copy(playerQueue = it.playerQueue + music) }
        controller?.addMediaItem(mediaItem)
    }

    actual fun removeFromQueue(index: Int) {
        if (index in _queueState.value.playerQueue.indices) {
            _queueState.update {
                it.copy(
                    playerQueue = it.playerQueue.toMutableList().apply { removeAt(index) }
                )
            }
            controller?.removeMediaItem(index)
        }
    }

    actual fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int
    ) {
        when (event) {
            OMPlayerEvent.Previous -> controller?.seekToPrevious()
            OMPlayerEvent.Next -> controller?.seekToNext()
            OMPlayerEvent.PlayPause -> {
                if (controller?.isPlaying == true) {
                    controller!!.pause()
                } else {
                    controller?.play()
                }
            }

            OMPlayerEvent.SelectedAudioChange -> {
                controller?.seekToDefaultPosition(selectedAudioIndex)
                controller?.playWhenReady = true
            }

            is OMPlayerEvent.SeekTo -> {
                controller?.seekTo(
                    controller?.duration?.times(event.progress)?.toLong() ?: 0
                )
            }
        }
    }

    actual fun updateProgress() {
        _playbackState.update {
            it.copy(
                progress = OMPlayerPlaybackState.Progress(
                    progress = ((controller?.currentPosition ?: 0) / (controller?.duration?.toFloat() ?: 0f)).coerceIn(0f, 1f),
                    time = controller?.currentPosition?.milliseconds ?: (0).milliseconds,
                    timestamp = System.currentTimeMillis()
                ),
            )
        }
    }

    private fun updateNotificationMetadata() {
        controllerScope.launch {
            appearanceSettings.collect { settings ->
                val controller = controller ?: return@collect
                val currentIndex = controller.currentMediaItemIndex
                val mediaItemCount = controller.mediaItemCount

                if (mediaItemCount == 0 || currentIndex !in 0 until mediaItemCount) {
                    return@collect
                }

                val currentMediaItem = controller.getMediaItemAt(currentIndex)

                val metadataExtras = currentMediaItem.mediaMetadata.extras ?: return@collect
                val newTitle = metadataExtras.getString(
                    if (settings.showInOriginalLang) "unicodeTitle" else "title"
                ) ?: return@collect
                val newArtist = metadataExtras.getString(
                    if (settings.showInOriginalLang) "unicodeArtist" else "artist"
                ) ?: return@collect

                val newMediaItem = currentMediaItem.buildUpon()
                    .setMediaMetadata(
                        currentMediaItem.mediaMetadata.buildUpon()
                            .setTitle(newTitle)
                            .setArtist(newArtist)
                            .build()
                    )
                    .build()

                controller.replaceMediaItem(currentIndex, newMediaItem)
            }
        }
    }

    actual fun releasePlayer() {
        MediaController.releaseFuture(controllerFuture)
    }
}