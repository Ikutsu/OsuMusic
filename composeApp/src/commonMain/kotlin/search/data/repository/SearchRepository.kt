package io.ikutsu.osumusic.search.data.repository

import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.search.data.datasource.SearchLocalDataSource
import io.ikutsu.osumusic.search.data.datasource.SearchRemoteDataSource
import io.ikutsu.osumusic.search.data.model.SearchHistory
import io.realm.kotlin.ext.toRealmList

class SearchRepository(
    private val remote: SearchRemoteDataSource,
    private val local: SearchLocalDataSource
) {

    private var lastQuery: String = ""
    private var latestSearch: Result<List<DiffBeatmapState>> = Result.success(emptyList())

    suspend fun search(apiType: BeatmapSource, query: String): Result<List<DiffBeatmapState>> {
        return if (query == lastQuery) {
            latestSearch
        } else {
            val result = remote.search(apiType, query)
            lastQuery = query
            latestSearch = result
            result
        }
    }

    suspend fun saveSearchHistory(beatmap: DiffBeatmapState) {
        local.saveSearchHistory(
            SearchHistory().apply {
                beatmapId = beatmap.beatmapId
                title = beatmap.title
                titleUnicode = beatmap.titleUnicode
                artist = beatmap.artist
                artistUnicode = beatmap.artistUnicode
                creator = beatmap.creator
                difficulty = beatmap.diff.toRealmList()
                coverUrl = beatmap.coverUrl
                audioUrl = beatmap.audioUrl
            }
        )
    }

    fun getSearchHistory() = local.getSearchHistory()
}