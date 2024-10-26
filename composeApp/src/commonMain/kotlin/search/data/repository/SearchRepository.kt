package io.ikutsu.osumusic.search.data.repository

import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.search.data.datasource.SearchRemoteDataSource

class SearchRepository(
    private val dataSource: SearchRemoteDataSource
) {

    private var lastQuery: String = ""
    private var latestSearch: Result<List<DiffBeatmapState>> = Result.success(emptyList())

    suspend fun search(apiType: BeatmapSource, query: String): Result<List<DiffBeatmapState>> {
        return if (query == lastQuery) {
            latestSearch
        } else {
            val result = dataSource.search(apiType, query)
            lastQuery = query
            latestSearch = result
            result
        }
    }
}