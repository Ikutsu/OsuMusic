
package io.ikutsu.osumusic.search.data.datasource

import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.data.OsuDirect
import io.ikutsu.osumusic.core.data.getBeatmapCoverUrl
import io.ikutsu.osumusic.core.data.getBeatmapFileUrl
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.search.data.api.OsuDirectBeatmapListRequest
import io.ikutsu.osumusic.search.data.api.OsuDirectBeatmapSearchApi
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapListRequest
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapSearchApi
import io.ktor.serialization.JsonConvertException

class SearchRemoteDataSource(
    private val sayoApi: SayobotBeatmapSearchApi,
    private val osuDirectApi: OsuDirectBeatmapSearchApi
) {
    suspend fun search(
        apiType: BeatmapSource,
        query: String
    ): Result<List<DiffBeatmapState>> {
        when (apiType) {
            BeatmapSource.SAYOBOT -> {
                val response = sayoApi.search(
                    SayobotBeatmapListRequest(
                        keyword = query
                    )
                )

                return response.mapCatching { res ->
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
                }.recoverCatching {
                    if (it is JsonConvertException) {
                        emptyList()
                    } else {
                        throw it
                    }
                }
            }
            BeatmapSource.OSU_DIRECT -> {
                val response = osuDirectApi.search(
                    OsuDirectBeatmapListRequest(
                        query = query
                    )
                )

                return response.mapCatching { res ->
                    res.beatmapSets.map {
                        DiffBeatmapState(
                            beatmapId = it.id,
                            audioUrl = OsuDirect.getAudioUrl(it.beatmaps.first().id.toString()),
                            coverUrl = it.covers.cover,
                            title = it.title,
                            artist = it.artist,
                            creator = it.creator,
                            diff = it.beatmaps.map { diff -> diff.difficultyRating.toFloat() }
                        )
                    }
                }
            }
        }
    }
}