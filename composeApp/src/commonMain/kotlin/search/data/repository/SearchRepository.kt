package io.ikutsu.osumusic.search.data.repository

import io.ikutsu.osumusic.core.domain.AllDiffBeatmapState
import io.ikutsu.osumusic.search.data.datasource.ApiType
import io.ikutsu.osumusic.search.data.datasource.SearchRemoteDataSource

class SearchRepository(
    private val dataSource: SearchRemoteDataSource
) {

    private var lastQuery: String = ""
    private var latestSearch: Result<List<AllDiffBeatmapState>> = Result.success(emptyList())

    suspend fun search(apiType: ApiType, query: String): Result<List<AllDiffBeatmapState>> {
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