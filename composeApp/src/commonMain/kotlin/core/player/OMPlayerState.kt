package io.ikutsu.osumusic.core.player

sealed class OMPlayerState {
    data object Buffering : OMPlayerState()
    data object Playing : OMPlayerState()
    data object Paused : OMPlayerState()
    data object Stopped : OMPlayerState()
    data object Error : OMPlayerState()
}