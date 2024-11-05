package io.ikutsu.osumusic.search.data.api

import io.ikutsu.osumusic.core.data.OsuDirect
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
                    parameter("q", query.query)
                    parameter("sort", query.sort)
                    parameter("amount", query.amount)
                    parameter("offset", query.offset)
                    parameter("status", query.status)
                    parameter("mode", query.mode)
                    contentType(ContentType.Application.Json)
                }.also {
                    println(it.call.request.url)
                }.body<List<OsuDirectBeatmapSearchResponse.OsuDirectBeatmapSet>>().let {
                    OsuDirectBeatmapSearchResponse(it)
                }
            }
        }
}