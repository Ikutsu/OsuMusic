package io.ikutsu.osumusic.core.data

val OSU_ASSETS_BASE_URL = "https://assets.ppy.sh"
fun getBeatmapBackgroundUrl(beatmapSetId: Int) =
    "$OSU_ASSETS_BASE_URL/beatmaps/$beatmapSetId/covers/raw.jpg"