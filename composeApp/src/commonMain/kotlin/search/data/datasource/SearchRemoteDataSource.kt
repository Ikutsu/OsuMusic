
package io.ikutsu.osumusic.search.data.datasource

import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.data.OsuDirect
import io.ikutsu.osumusic.core.data.SayoBot
import io.ikutsu.osumusic.core.domain.DiffBeatmapState
import io.ikutsu.osumusic.search.data.api.OsuDirectBeatmapSearchApi
import io.ikutsu.osumusic.search.data.api.OsuDirectBeatmapSearchRequest
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapSearchApi
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapSearchRequest
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
                    SayobotBeatmapSearchRequest(
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
                            audioUrl = SayoBot.getBeatmapFileUrl(it.sid, audioFile.orEmpty()),
                            coverUrl = SayoBot.getBeatmapCoverUrl(it.sid),
                            title = it.title,
                            titleUnicode = it.titleUnicode.ifBlank { it.title },
                            artist = it.artist,
                            artistUnicode = it.artistUnicode.ifBlank { it.artist },
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
                    OsuDirectBeatmapSearchRequest(
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
                            titleUnicode = it.titleUnicode,
                            artist = it.artist,
                            artistUnicode = it.artistUnicode,
                            creator = it.creator,
                            diff = it.beatmaps.map { diff -> diff.difficultyRating.toFloat() }
                        )
                    }
                }
            }
        }
    }
}