package io.ikutsu.osumusic.core.domain

data class DiffBeatmapState(
    val beatmapId: Int,
    val audioUrl: String,
    val coverUrl: String,
    val title: String,
    val titleUnicode: String,
    val artist: String,
    val artistUnicode: String,
    val creator: String,
    val diff: List<Float>
)