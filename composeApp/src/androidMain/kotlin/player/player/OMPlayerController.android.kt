package io.ikutsu.osumusic.player.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.core.bundle.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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
import io.ikutsu.osumusic.player.service.OMPlayerService

actual class OMPlayerController(context: Context) {

    private var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null
    val handler = Handler(Looper.getMainLooper())

    private val playQueue: MutableList<Music> = mutableListOf()
    private var currentQueueIndex: Int = 0

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, OMPlayerService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
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
                    val errorMsg = when(error?.errorCode) {
                        null -> return
                        PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> "Source unavailable"
                        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "Network connection failed"
                        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> "Network connection timeout"
                        else -> "Player error"
                    }
                    listener.currentPlayerState(OMPlayerState.Error)
                    listener.onError(errorMsg)
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
    }

    private fun updateMediaItems() {
        val mediaItems = playQueue.map { music ->
            MediaItem.Builder()
                .setMediaId(music.source)
                .setUri(music.source)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(music.title)
                        .setArtist(music.artist)
                        .setArtworkUri(Uri.parse(music.coverUrl))
                        .setExtras(
                            bundleOf(
                                "diff" to music.diff,
                                "backgroundUrl" to music.backgroundUrl
                            )
                        )
                        .build()
                )
                .build()
        }

        controller?.setMediaItems(mediaItems)
    }

    actual fun addToQueue(music: Music) {
        val mediaItem = MediaItem.Builder()
            .setMediaId(music.source)
            .setUri(music.source)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(music.title)
                    .setArtist(music.artist)
                    .setArtworkUri(Uri.parse(music.coverUrl))
                    .setExtras(
                        bundleOf(
                            "diff" to music.diff,
                            "backgroundUrl" to music.backgroundUrl
                        )
                    )
                    .build()
            )
            .build()

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

    actual fun release() {
        MediaController.releaseFuture(controllerFuture)
    }

}