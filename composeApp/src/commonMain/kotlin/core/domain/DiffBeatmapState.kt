package io.ikutsu.osumusic.core.domain

data class DiffBeatmapState(
    val beatmapId: Int,
    val audioUrl: String,
    val coverUrl: String,
    val title: String,
    val artist: String,
    val creator: String,
    val diff: List<Float>
)