package io.ikutsu.osumusic.player.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.core.bundle.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_IDLE
import androidx.media3.common.util.UnstableApi
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

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, OMPlayerService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
    }

    actual fun registerListener(listener: OMPlayerListener) {
        controllerFuture.addListener({
            controller?.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    listener.onError()
                }

                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    with(player) {
                        listener.currentPlayerState(
                            when (playbackState) {
                                STATE_IDLE -> OMPlayerState.Stopped
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
                    listener.onError()
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

    @OptIn(UnstableApi::class)
    actual fun addPlayerItem(
        musics: List<Music>
    ) {
        val mediaItem = musics.map { music ->
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

        controller?.setMediaItems(mediaItem)

        controller?.playWhenReady = true
        controller?.prepare()
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