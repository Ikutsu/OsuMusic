package io.ikutsu.osumusic.core.domain

data class SingleDiffBeatmapState(
    val beatmapId: Int,
    val coverUrl: String,
    val title: String,
    val artist: String,
    val diff: Float
)

data class AllDiffBeatmapState(
    val beatmapId: Int,
    val coverUrl: String,
    val title: String,
    val artist: String,
    val diff: List<Float>
)