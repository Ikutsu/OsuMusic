package io.ikutsu.osumusic.player.player

import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.*
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.player.service.OMPlayerService
import io.ikutsu.osumusic.setting.data.AppearanceSettings
import io.ikutsu.osumusic.setting.data.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

actual class OMPlayerController(
    context: Context,
    private val settingRepository: SettingRepository
) {

    private var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null
    val handler = Handler(Looper.getMainLooper())

    private val playQueue: MutableList<Music> = mutableListOf()
    private var currentQueueIndex: Int = 0

    private val appearanceSettings: MutableStateFlow<AppearanceSettings> = MutableStateFlow(AppearanceSettings(false))

    private val controllerJob = SupervisorJob()
    private val controllerScope = CoroutineScope(Dispatchers.Main + controllerJob)

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, OMPlayerService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerScope.launch {
            settingRepository.getAppearanceSettings().collect { settings ->
                appearanceSettings.update { it }
            }
        }
    }

    actual fun registerListener(listener: OMPlayerListener) {
        controllerFuture.addListener({
            controller?.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    with(player) {
                        listener.currentPlayerState(
                            when (playbackState) {
                                STATE_IDLE -> return
                                STATE_BUFFERING -> OMPlayerState.Buffering
                                STATE_ENDED -> OMPlayerState.Stopped
                                else -> if (isPlaying) OMPlayerState.Playing else OMPlayerState.Paused
                            }
                        )

                        listener.currentMusic(currentMediaItem?.toMusic())
                        listener.totalDuration(duration.coerceAtLeast(0L))
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
                    listener.currentPlayerState(OMPlayerState.Error)
                    listener.onError(errorMsg)
                }

                override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                    super.onTimelineChanged(timeline, reason)
                    listener.onQueueChanged(playQueue)
                }
            })
            handler.post(object : Runnable {
                override fun run() {
                    listener.onProgress(controller?.currentPosition ?: 0)
                    handler.postDelayed(this, 1000)
                }
            })
        }, MoreExecutors.directExecutor())
    }

    actual fun setPlayerItem(
        musics: List<Music>
    ) {
        playQueue.clear()
        playQueue.addAll(musics)
        currentQueueIndex = 0
        updateMediaItems()
        controller?.playWhenReady = true
        controller?.prepare()
        updateNotificationMetadata()
    }

    private fun updateMediaItems() {
        val mediaItems = playQueue.map {
            it.toMediaItem(appearanceSettings.value.showInOriginalLang)
        }
        controller?.setMediaItems(mediaItems)
    }

    actual fun addToQueue(music: Music) {
        val mediaItem = music.toMediaItem(appearanceSettings.value.showInOriginalLang)

        playQueue.add(music)
        controller?.addMediaItem(mediaItem)
    }

    actual fun removeFromQueue(index: Int) {
        if (index in playQueue.indices) {
            playQueue.removeAt(index)
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

    private fun updateNotificationMetadata() {
        controllerScope.launch {
            appearanceSettings.collect { settings ->
                if (controller == null) return@collect

                val currentMediaItem = controller?.currentMediaItem
                if (currentMediaItem != null) {
                    val newMediaItem = currentMediaItem.buildUpon()
                        .setMediaMetadata(
                            controller?.mediaMetadata?.buildUpon()
                                ?.setTitle(
                                    currentMediaItem.mediaMetadata.extras?.getString(
                                        if (settings.showInOriginalLang) {
                                            "unicodeTitle"
                                        } else {
                                            "title"
                                        }
                                    )
                                )
                                ?.setArtist(
                                    currentMediaItem.mediaMetadata.extras?.getString(
                                        if (settings.showInOriginalLang) {
                                            "unicodeArtist"
                                        } else {
                                            "artist"
                                        }
                                    )
                                )
                            !!.build()
                        )
                        .build()
                    controller?.replaceMediaItem(controller?.currentMediaItemIndex!!, newMediaItem)
                }
            }
        }
    }

    actual fun release() {
        MediaController.releaseFuture(controllerFuture)
    }

}