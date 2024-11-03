package io.ikutsu.osumusic.core.data

object OsuDirect {
    const val OSUDIRECT_API_BASE_URL = "https://osu.direct"
    const val OSUDIRECT_API_SEARCH = "$OSUDIRECT_API_BASE_URL/api/v2/search"

    fun getAudioUrl(mapId: String) =
        "$OSUDIRECT_API_BASE_URL/api/media/audio/$mapId"
}