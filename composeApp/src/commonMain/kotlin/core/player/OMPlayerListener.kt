package io.ikutsu.osumusic.core.player

import io.ikutsu.osumusic.core.domain.Music

interface OMPlayerListener {
    fun onProgress(progress: Long)
    fun totalDuration(duration: Long)
    fun currentPlayerState(state: OMPlayerState)
    fun currentMusic(music: Music?)
    fun onQueueChanged(queue: List<Music>)
    fun onError(message: String)
}