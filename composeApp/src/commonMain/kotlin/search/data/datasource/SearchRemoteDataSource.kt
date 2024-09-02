
package io.ikutsu.osumusic.search.data.datasource

import io.ikutsu.osumusic.core.data.getBeatmapCoverUrl
import io.ikutsu.osumusic.core.data.getBeatmapFileUrl
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapListRequest
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapSearchApi
import io.ktor.serialization.JsonConvertException

class SearchRemoteDataSource(
    private val sayoApi: SayobotBeatmapSearchApi
) {
    suspend fun search(
        apiType: ApiType,
        query: String
    ): Result<List<DiffBeatmapState>> {
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
                        val beatmapSetDetail = sayoApi.getBeatmapSetDetail(
                            it.sid
                        ).getOrNull()

                        val diffs = beatmapSetDetail?.data?.bidData?.map { diff -> diff.star }

                        val audioFile = beatmapSetDetail?.data?.bidData?.first()?.audio

                        DiffBeatmapState(
                            beatmapId = it.sid,
                            audioUrl = getBeatmapFileUrl(it.sid, audioFile.orEmpty()),
                            coverUrl = getBeatmapCoverUrl(it.sid),
                            title = it.titleUnicode.ifBlank { it.title },
                            artist = it.artistUnicode.ifBlank { it.artist },
                            creator = it.creator,
                            diff = diffs.orEmpty()
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
