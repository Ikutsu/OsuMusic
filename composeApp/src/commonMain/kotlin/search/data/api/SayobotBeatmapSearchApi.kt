package io.ikutsu.osumusic.search.data.api

import io.ikutsu.osumusic.core.data.SAYOBOT_API_DETAIL
import io.ikutsu.osumusic.core.data.SAYOBOT_API_SEARCH
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
) : BeatmapSearchApi<SayobotBeatmapListRequest, SayobotBeatmapListResponse> {

    override suspend fun search(
        query: SayobotBeatmapListRequest
    ): Result<SayobotBeatmapListResponse> =
        runCatching {
            withContext(Dispatchers.IO) {
                httpClient.post {
                    url(SAYOBOT_API_SEARCH)
                    contentType(ContentType.Application.Json)
                    setBody(query)
                }.body<SayobotBeatmapListResponse>()
            }
        }

    suspend fun getBeatmapSetDetail(beatmapSetId: Int): Result<SayobotBeatmapSetDetailResponse> =
        runCatching {
            withContext(Dispatchers.IO) {
                httpClient.get {
                    url(SAYOBOT_API_DETAIL)
                    parameter("s", "$beatmapSetId")
                    contentType(ContentType.Application.Json)
                }.let {
                    Json.decodeFromString<SayobotBeatmapSetDetailResponse>(replaceQuotes(it.bodyAsText()))
                }
            }
        }
}

fun replaceQuotes(input: String): String {
    return input.replace(Regex("(?<![:,{])\"(?![:,}])")) {
        "\\\""
    }
}
