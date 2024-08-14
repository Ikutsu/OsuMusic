package io.ikutsu.osumusic.search.data.api

interface BeatmapSearchApi<req: BeatmapListRequest, res: BeatmapListResponse> {
    suspend fun search(query: req): Result<res>
}