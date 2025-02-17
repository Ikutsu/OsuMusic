package io.ikutsu.osumusic.search.data.remote.api.osu_direct

import io.ikutsu.osumusic.core.data.remote.api.OsuDirect
import io.ikutsu.osumusic.search.data.remote.api.BeatmapSearchApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class OsuDirectBeatmapSearchApi (
    private val httpClient: HttpClient
): BeatmapSearchApi<OsuDirectBeatmapSearchRequest, OsuDirectBeatmapSearchResponse> {
    override suspend fun search(
        query: OsuDirectBeatmapSearchRequest
    ): Result<OsuDirectBeatmapSearchResponse> =
        withContext(Dispatchers.IO) {
            runCatching {
                httpClient.get {
                    url(OsuDirect.OSUDIRECT_API_SEARCH)
                    parameter("q", "[is_scoreable = true]${query.query}")
                    parameter("sort", query.sort)
                    parameter("amount", query.amount)
                    parameter("offset", query.offset)
                    parameter("status", query.status)
                    parameter("mode", query.mode)
                    contentType(ContentType.Application.Json)
                }.body<List<OsuDirectBeatmapSearchResponse.OsuDirectBeatmapSet>>().let {
                    OsuDirectBeatmapSearchResponse(it)
                }
            }
        }
}