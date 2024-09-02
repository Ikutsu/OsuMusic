package io.ikutsu.osumusic.core.domain

data class Music(
    val title: String,
    val artist: String,
    val creator: String,
    val diff: Float,
    val coverUrl: String,
    val backgroundUrl: String,
    val source: String
    // video
    // storyboard
    // hitsound
)
