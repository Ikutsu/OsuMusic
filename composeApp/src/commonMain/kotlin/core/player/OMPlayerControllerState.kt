package io.ikutsu.osumusic.core.player

import io.ikutsu.osumusic.core.domain.Music
import kotlin.time.Duration

data class OMPlayerQueueState(
    val currentMusic: Music? = null,
    val playerQueue: List<Music> = listOf(),
)

data class OMPlayerPlaybackState(
    val progress: Progress = Progress(),
    val duration: Duration = Duration.ZERO,
    val playerState: OMPlayerState? = null,
    val errorMessage: String? = ""
) {
    data class Progress(
        val progress: Float = 0f,
        val time: Duration = Duration.ZERO,
        val timestamp: Long = 0L
    )
}