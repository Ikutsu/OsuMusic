package io.ikutsu.osumusic.core.domain

data class Music(
    val title: String,
    val unicodeTitle: String,
    val artist: String,
    val unicodeArtist: String,
    val creator: String,
    val difficulty: Float,
    val coverUrl: String,
    val backgroundUrl: String,
    val source: String
    // video
    // storyboard
    // hitsound
)
