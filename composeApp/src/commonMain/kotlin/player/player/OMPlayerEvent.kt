package io.ikutsu.osumusic.player.player

sealed class OMPlayerEvent {
    data object PlayPause : OMPlayerEvent()
    data object Next : OMPlayerEvent()
    data object Previous : OMPlayerEvent()
    data object SelectedAudioChange : OMPlayerEvent()
    data class SeekTo(val progress: Float) : OMPlayerEvent()
}