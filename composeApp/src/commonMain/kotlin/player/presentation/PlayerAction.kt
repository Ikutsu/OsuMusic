package io.ikutsu.osumusic.player.presentation

sealed interface PlayerAction {
    data object onBackClick : PlayerAction
    data class onQueueItemClick(val index: Int) : PlayerAction
    data object onPlayPauseClick : PlayerAction
    data object onSkipNextClick : PlayerAction
    data object onSkipPreviousClick : PlayerAction
    data class onProgressChange(val position: Float) : PlayerAction
    data object onSeek: PlayerAction
}