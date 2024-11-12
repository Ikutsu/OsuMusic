package io.ikutsu.osumusic.player.presentation

import io.ikutsu.osumusic.core.domain.Music
import io.ikutsu.osumusic.player.player.OMPlayerState

data class PlayerUiState(
    val playerState: OMPlayerState? = null,
    val currentMusic: Music? = null,
    val currentProgress: Float = 0f,
    val currentProgressInLong: Long = 0L,
    val duration: Long = 0L,
    val playerQueue: List<Music> = listOf(),
    val isError: Boolean = false,
    val errorMessage: String? = ""
)