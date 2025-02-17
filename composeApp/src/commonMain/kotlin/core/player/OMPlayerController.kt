package io.ikutsu.osumusic.core.player

import io.ikutsu.osumusic.core.domain.Music

expect class OMPlayerController {

    fun registerListener(listener: OMPlayerListener)

    fun setPlayerItem(musics: List<Music>)

    fun addToQueue(music: Music)

    fun removeFromQueue(index: Int)

    fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int = -1
    )

    fun release()
}