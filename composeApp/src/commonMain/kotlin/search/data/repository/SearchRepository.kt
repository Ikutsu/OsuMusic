package io.ikutsu.osumusic.search.data.repository

import io.ikutsu.osumusic.core.domain.BeatmapMetadata
import io.ikutsu.osumusic.core.domain.BeatmapSource
import io.ikutsu.osumusic.core.domain.toSearchHistory
import io.ikutsu.osumusic.search.data.local.SearchLocalDataSource
import io.ikutsu.osumusic.search.data.remote.SearchRemoteDataSource
import io.ikutsu.osumusic.setting.data.SettingRepository
import kotlinx.coroutines.flow.first

class SearchRepository(
    private val remote: SearchRemoteDataSource,
    private val local: SearchLocalDataSource,
    private val settingRepository: SettingRepository
) {

    private var lastQuery: String = ""
    private var lastUsedSource: BeatmapSource? = null
    private var latestSearch: Result<List<BeatmapMetadata>> = Result.success(emptyList())

    suspend fun search(query: String): Result<List<BeatmapMetadata>> {
        val apiType = BeatmapSource.valueOf(settingRepository.getSearchSettings().first().beatmapSource)
        return if (query == lastQuery && apiType == lastUsedSource) {
            latestSearch
        } else {
            val result = remote.search(apiType, query)
            lastQuery = query
            lastUsedSource = apiType
            latestSearch = result
            result
        }
    }

    suspend fun saveSearchHistory(beatmap: BeatmapMetadata) {
        local.saveSearchHistory(beatmap.toSearchHistory())
    }

    fun getSearchHistory() = local.getSearchHistory()
}