package io.ikutsu.osumusic.player.player

import io.ikutsu.osumusic.core.domain.Music

interface OMPlayerListener {
    fun onProgress(progress: Long)
    fun totalDuration(duration: Long)
    fun currentPlayerState(state: OMPlayerState)
    fun currentMusic(music: Music?)
    fun onError(message: String)
}