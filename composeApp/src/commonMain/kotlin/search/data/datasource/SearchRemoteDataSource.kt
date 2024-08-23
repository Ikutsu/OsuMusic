
package io.ikutsu.osumusic.search.data.datasource

import io.ikutsu.osumusic.core.data.getBeatmapCoverUrl
import io.ikutsu.osumusic.core.domain.AllDiffBeatmapState
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapListRequest
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapSearchApi
import io.ktor.serialization.JsonConvertException

class SearchRemoteDataSource(
    private val sayoApi: SayobotBeatmapSearchApi
) {
    suspend fun search(
        apiType: ApiType,
        query: String
    ): Result<List<AllDiffBeatmapState>> {
        when (apiType) {
            ApiType.SAYOBOT -> {
                val response = sayoApi.search(
                    SayobotBeatmapListRequest(
                        keyword = query
                    )
                )

                return response.map { res ->
                    res.data!!.sortedByDescending {
                        it.playCount
                    }.map {
                        val diffs = sayoApi.getBeatmapSetDetail(
                            it.sid
                        ).getOrThrow().data.bidData.map {
                            diff -> diff.star
                        }

                        AllDiffBeatmapState(
                            beatmapId = it.sid,
                            coverUrl = getBeatmapCoverUrl(it.sid),
                            title = it.titleUnicode.ifBlank { it.title },
                            artist = it.artistUnicode.ifBlank { it.artist },
                            diff = diffs
                        )
                    }
                }.recover {
                    if (it is JsonConvertException) {
                        emptyList()
                    } else {
                        throw it
                    }
                }
            }
        }
    }
}

enum class ApiType {
    SAYOBOT
}
