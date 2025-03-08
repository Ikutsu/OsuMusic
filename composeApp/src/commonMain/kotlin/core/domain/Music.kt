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

fun Music.toBeatmapMetadata(): BeatmapMetadata {
    return BeatmapMetadata(
        beatmapId = 0,
        audioUrl = this.source,
        coverUrl = this.coverUrl,
        title = this.title,
        unicodeTitle = this.unicodeTitle,
        artist = this.artist,
        unicodeArtist = this.unicodeArtist,
        creator = this.creator,
        difficulties = listOf(this.difficulty)
    )
}