package io.ikutsu.osumusic.core.player

import io.ikutsu.osumusic.core.domain.Music
import kotlinx.coroutines.flow.StateFlow

expect class OMPlayerController {

    val queueState: StateFlow<OMPlayerQueueState>
    val playbackState: StateFlow<OMPlayerPlaybackState>

    fun initializePlayer()

    fun setPlayerItem(musics: List<Music>)

    fun addToQueue(music: Music)

    fun removeFromQueue(index: Int)

    fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int = -1
    )

    fun updateProgress()

    fun releasePlayer()

}