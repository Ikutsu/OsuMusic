package io.ikutsu.osumusic.core.data.repository

import io.ikutsu.osumusic.core.data.datasource.PlayHistoryDataSource
import io.ikutsu.osumusic.core.data.model.PlayHistory
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.realm.kotlin.ext.toRealmList

class PlayHistoryRepository(
    private val playHistoryDataSource: PlayHistoryDataSource
) {
    suspend fun savePlayHistory(beatmapState: DiffBeatmapState) {
        playHistoryDataSource.savePlayHistory(
            PlayHistory().apply {
                beatmapId = beatmapState.beatmapId
                title = beatmapState.title
                artist = beatmapState.artist
                creator = beatmapState.creator
                difficulty = beatmapState.diff.toRealmList()
                coverUrl = beatmapState.coverUrl
                audioUrl = beatmapState.audioUrl
            }
        )
    }

    fun getPlayHistory() = playHistoryDataSource.getPlayHistory()
}