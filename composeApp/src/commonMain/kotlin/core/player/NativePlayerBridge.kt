package io.ikutsu.osumusic.core.player

import io.ikutsu.osumusic.core.domain.Music

interface NativePlayerBridge {
    fun setSource(music: Music)
    fun play()
    fun pause()
    fun seekTo(position: Double)
    fun getDuration(): Double
    fun getCurrentPosition(): Double
    fun isPlaying(): Boolean
    fun release()
    fun setOnStateChangeListener(listener: (OMPlayerState) -> Unit)
    fun setOnMusicChangeListener(listener: (Boolean) -> Unit)
    fun setOnFinishListener(listener: () -> Unit)
    fun setOnErrorListener(listener: (String) -> Unit)
}