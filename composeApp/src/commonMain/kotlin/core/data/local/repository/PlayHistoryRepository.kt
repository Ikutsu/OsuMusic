package io.ikutsu.osumusic.core.data.local.repository

import io.ikutsu.osumusic.core.data.local.datasource.PlayHistoryDataSource
import io.ikutsu.osumusic.core.domain.BeatmapMetadata
import io.ikutsu.osumusic.core.domain.toPlayHistory

class PlayHistoryRepository(
    private val playHistoryDataSource: PlayHistoryDataSource
) {
    suspend fun savePlayHistory(beatmapMetadata: BeatmapMetadata) {
        playHistoryDataSource.savePlayHistory(beatmapMetadata.toPlayHistory())
    }

    fun getPlayHistory() = playHistoryDataSource.getPlayHistory()
}