package io.ikutsu.osumusic.core.data.model

import io.ikutsu.osumusic.core.data.remote.api.Osu
import io.ikutsu.osumusic.core.domain.BeatmapMetadata
import io.ikutsu.osumusic.core.domain.Music
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PlayHistoryEntity: RealmObject {
    @PrimaryKey
    var beatmapId: Int = 0
    var title: String = ""
    var unicodeTitle: String = ""
    var artist: String = ""
    var unicodeArtist: String = ""
    var creator: String = ""
    var difficulty: Float = 0f
    var coverUrl: String = ""
    var audioUrl: String = ""
    var addedAt: RealmInstant = RealmInstant.now()
}

fun PlayHistoryEntity.toBeatmapMetadata(): BeatmapMetadata {
    return BeatmapMetadata(
        beatmapId = this.beatmapId,
        audioUrl = this.audioUrl,
        coverUrl = this.coverUrl,
        title = this.title,
        unicodeTitle = this.unicodeTitle,
        artist = this.artist,
        unicodeArtist = this.unicodeArtist,
        creator = this.creator,
        difficulties = listOf(this.difficulty)
    )
}

fun PlayHistoryEntity.toMusic(): Music {
    return Music(
        title = this.title,
        unicodeTitle = this.unicodeTitle,
        artist = this.artist,
        unicodeArtist = this.unicodeArtist,
        creator = this.creator,
        difficulty = this.difficulty,
        coverUrl = this.coverUrl,
        backgroundUrl = Osu.getBeatmapBackgroundUrl(this.beatmapId),
        source = this.audioUrl
    )
}