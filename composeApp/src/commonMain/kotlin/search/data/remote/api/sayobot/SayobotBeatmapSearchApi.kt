package io.ikutsu.osumusic.search.data.remote.api.sayobot

import io.ikutsu.osumusic.core.data.SayoBot
import io.ikutsu.osumusic.search.data.remote.api.BeatmapSearchApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class SayobotBeatmapSearchApi(
    private val httpClient: HttpClient
) : BeatmapSearchApi<SayobotBeatmapSearchRequest, SayobotBeatmapSearchResponse> {

    override suspend fun search(
        query: SayobotBeatmapSearchRequest
    ): Result<SayobotBeatmapSearchResponse> =
        runCatching {
            withContext(Dispatchers.IO) {
                httpClient.post {
                    url(SayoBot.SAYOBOT_API_SEARCH)
                    contentType(ContentType.Application.Json)
                    setBody(query)
                }.body<SayobotBeatmapSearchResponse>()
            }
        }

    suspend fun getBeatmapSetDetail(beatmapSetId: Int): Result<SayobotBeatmapSetDetailResponse> =
        runCatching {
            withContext(Dispatchers.IO) {
                httpClient.get {
                    url(SayoBot.SAYOBOT_API_DETAIL)
                    parameter("K", "$beatmapSetId")
                    contentType(ContentType.Application.Json)
                }.let {
                    Json.decodeFromString<SayobotBeatmapSetDetailResponse>(it.bodyAsText())
                }
            }
        }
}