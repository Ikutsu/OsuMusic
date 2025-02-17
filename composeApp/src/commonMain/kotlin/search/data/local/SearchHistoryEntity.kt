package io.ikutsu.osumusic.search.data.local

import io.ikutsu.osumusic.core.domain.BeatmapMetadata
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class SearchHistoryEntity: RealmObject {
    @PrimaryKey
    var beatmapId: Int = 0
    var title: String = ""
    var unicodeTitle: String = ""
    var artist: String = ""
    var unicodeArtist: String = ""
    var creator: String = ""
    var difficulty: RealmList<Float> = realmListOf()
    var coverUrl: String = ""
    var audioUrl: String = ""
    var addedAt: RealmInstant = RealmInstant.now()
}

fun SearchHistoryEntity.toBeatmapMetadata(): BeatmapMetadata {
    return BeatmapMetadata(
        beatmapId = this.beatmapId,
        audioUrl = this.audioUrl,
        coverUrl = this.coverUrl,
        title = this.title,
        unicodeTitle = this.unicodeTitle,
        artist = this.artist,
        unicodeArtist = this.unicodeArtist,
        creator = this.creator,
        difficulties = this.difficulty.toList()
    )
}