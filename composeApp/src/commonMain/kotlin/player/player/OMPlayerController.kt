package io.ikutsu.osumusic.player.player

import io.ikutsu.osumusic.core.domain.Music

expect class OMPlayerController {

    fun registerListener(listener: OMPlayerListener)

    fun addPlayerItem(musics: List<Music>)

    fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int = -1
    )

    fun getCurrentPosition(): Long

    fun release()
}