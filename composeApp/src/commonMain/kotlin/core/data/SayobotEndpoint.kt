package io.ikutsu.osumusic.core.data

const val SAYOBOT_API_BASE_URL = "https://api.sayobot.cn"
const val SAYOBOT_API_SEARCH = "$SAYOBOT_API_BASE_URL/?post"
const val SAYOBOT_API_DETAIL = "$SAYOBOT_API_BASE_URL/v2/beatmapinfo"

const val SAYOBOT_ASSETS_BASE_URL = "https://a.sayobot.cn"
fun getBeatmapCoverUrl(beatmapSetId: Int) =
    "$SAYOBOT_ASSETS_BASE_URL/beatmaps/$beatmapSetId/covers/cover.webp"

const val SAYOBOT_DOWNLOAD_BASE_URL = "https://dl.sayobot.cn"
fun getBeatmapFileUrl(beatmapSetId: Int, fileName: String) =
    "$SAYOBOT_DOWNLOAD_BASE_URL/beatmaps/files/$beatmapSetId/$fileName"