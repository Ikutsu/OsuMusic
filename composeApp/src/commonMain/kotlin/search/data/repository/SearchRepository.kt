package io.ikutsu.osumusic.search.data.repository

import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.search.data.datasource.SearchLocalDataSource
import io.ikutsu.osumusic.search.data.datasource.SearchRemoteDataSource
import io.ikutsu.osumusic.search.data.model.SearchHistory
import io.ikutsu.osumusic.setting.data.SettingRepository
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.first

class SearchRepository(
    private val remote: SearchRemoteDataSource,
    private val local: SearchLocalDataSource,
    private val settingRepository: SettingRepository
) {

    private var lastQuery: String = ""
    private var lastUsedSource: BeatmapSource = BeatmapSource.NO_SOURCE
    private var latestSearch: Result<List<DiffBeatmapState>> = Result.success(emptyList())

    suspend fun search(query: String): Result<List<DiffBeatmapState>> {
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