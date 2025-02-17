package io.ikutsu.osumusic.search.data.remote.api

interface BeatmapSearchApi<req: BeatmapSearchRequest, res: BeatmapSearchResponse> {
    suspend fun search(query: req): Result<res>
}