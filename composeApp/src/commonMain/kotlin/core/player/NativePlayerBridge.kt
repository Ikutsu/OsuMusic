package io.ikutsu.osumusic.core.player

interface NativePlayerBridge {
    fun setSource(url: String)
    fun play()
    fun pause()
    fun seekTo(position: Double)
    fun getDuration(): Double
    fun getCurrentPosition(): Double
    fun getPlaybackState(): OMPlayerState
    fun isPlaying(): Boolean
    fun release()
    fun setOnStateChangeListener(listener: (OMPlayerState) -> Unit)
    fun setOnFinishListener(listener: () -> Unit)
    fun setOnErrorListener(listener: (String) -> Unit)
}