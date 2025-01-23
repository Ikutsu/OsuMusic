package io.ikutsu.osumusic.core.data.model

import io.ikutsu.osumusic.core.data.Osu
import io.ikutsu.osumusic.core.domain.BeatmapMetadata
import io.ikutsu.osumusic.core.domain.Music
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PlayHistory: RealmObject {
    @PrimaryKey
    var beatmapId: Int = 0
    var title: String = ""
    var titleUnicode: String = ""
    var artist: String = ""
    var artistUnicode: String = ""
    var creator: String = ""
    var difficulty: RealmList<Float> = realmListOf()
    var coverUrl: String = ""
    var audioUrl: String = ""
    var addedAt: RealmInstant = RealmInstant.now()
}

fun PlayHistory.toBeatmapMetadata(): BeatmapMetadata {
    return BeatmapMetadata(
        beatmapId = this.beatmapId,
        audioUrl = this.audioUrl,
        coverUrl = this.coverUrl,
        title = this.title,
        unicodeTitle = this.titleUnicode,
        artist = this.artist,
        unicodeArtist = this.artistUnicode,
        creator = this.creator,
        difficulties = this.difficulty.toList()
    )
}

fun PlayHistory.toMusic(): Music {
    return Music(
        title = this.title,
        unicodeTitle = this.titleUnicode,
        artist = this.artist,
        unicodeArtist = this.artistUnicode,
        creator = this.creator,
        difficulty = this.difficulty.first(),
        coverUrl = this.coverUrl,
        backgroundUrl = Osu.getBeatmapBackgroundUrl(this.beatmapId),
        source = this.audioUrl
    )
}