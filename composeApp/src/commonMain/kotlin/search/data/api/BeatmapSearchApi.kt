package io.ikutsu.osumusic.search.data.api

interface BeatmapSearchApi<req: BeatmapSearchRequest, res: BeatmapSearchResponse> {
    suspend fun search(query: req): Result<res>
}