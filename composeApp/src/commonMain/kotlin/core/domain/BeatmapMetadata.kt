package io.ikutsu.osumusic.core.domain

import io.ikutsu.osumusic.core.data.Osu
import io.ikutsu.osumusic.core.data.model.PlayHistory
import io.ikutsu.osumusic.search.data.local.SearchHistoryEntity
import io.realm.kotlin.ext.toRealmList

data class BeatmapMetadata(
    val beatmapId: Int,
    val audioUrl: String,
    val coverUrl: String,
    val title: String,
    val unicodeTitle: String,
    val artist: String,
    val unicodeArtist: String,
    val creator: String,
    val difficulties: List<Float>
)

fun BeatmapMetadata.toMusic(): Music {
    return Music(
        title = this.title,
        unicodeTitle = this.unicodeTitle,
        artist = this.artist,
        unicodeArtist = this.unicodeArtist,
        creator = this.creator,
        difficulty = this.difficulties.first(),
        coverUrl = this.coverUrl,
        backgroundUrl = Osu.getBeatmapBackgroundUrl(this.beatmapId),
        source = this.audioUrl
    )
}

fun BeatmapMetadata.toSearchHistory(): SearchHistoryEntity {
    this.let {
        return SearchHistoryEntity().apply {
            beatmapId = it.beatmapId
            title = it.title
            unicodeTitle = it.unicodeTitle
            artist = it.artist
            unicodeArtist = it.unicodeArtist
            creator = it.creator
            difficulty = it.difficulties.toRealmList()
            coverUrl = it.coverUrl
            audioUrl = it.audioUrl
        }
    }
}

fun BeatmapMetadata.toPlayHistory(): PlayHistory {
    this.let {
        return PlayHistory().apply {
            beatmapId = it.beatmapId
            title = it.title
            titleUnicode = it.unicodeTitle
            artist = it.artist
            artistUnicode = it.unicodeArtist
            creator = it.creator
            difficulty = it.difficulties.toRealmList()
            coverUrl = it.coverUrl
            audioUrl = it.audioUrl
        }
    }
}