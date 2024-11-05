package io.ikutsu.osumusic.search.data.api

data class OsuDirectBeatmapSearchRequest(
    val query: String,
    val sort: String? = "favourite_count:desc",
    val amount: Int = 25,
    val offset: Int = 0,
    val status: String? = null,
    val mode: String? = null
): BeatmapSearchRequest
