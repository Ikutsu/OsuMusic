package io.ikutsu.osumusic.search.data.api

import io.ikutsu.osumusic.search.data.api.SayobotBeatmapListResponse.SayobotBeatmapSetInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from Sayobot Beatmap List Query
 * @param data List of SayobotBeatmapSetInfo
 * @param endId The starting offset of the next request. 0=Request completed
 * @param matchArtistResult Number of matched artist results
 * @param matchCreatorResult Number of matched creator results
 * @param matchTagsResult Number of matched tags results
 * @param matchTitleResult Number of matched title results
 * @param matchVersionResult Number of matched difficulties results
 * @param results The total number of results matching this keyword, and the match field only appears when offset=0
 * @param status Unknown
 * @param timeCost Time taken to process the request
 * @see SayobotBeatmapSetInfo
 */
@Serializable
data class SayobotBeatmapListResponse(
    @SerialName("data") val data: List<SayobotBeatmapSetInfo>?,
    @SerialName("endid") val endId: Int?,
    @SerialName("match_artist_results") val matchArtistResult: Int?,
    @SerialName("match_creator_results") val matchCreatorResult: Int?,
    @SerialName("match_tags_results") val matchTagsResult: Int?,
    @SerialName("match_title_results") val matchTitleResult: Int?,
    @SerialName("match_version_results") val matchVersionResult: Int?,
    @SerialName("results") val results: Int?,
    @SerialName("status") val status: Int,
    @SerialName("time_cost") val timeCost: Int?,
): BeatmapListResponse {
    /**
     * Sayobot Beatmap Info
     * @param sid Beatmap set Id
     * @param title Beatmap title
     * @param titleUnicode Beatmap title in Unicode
     * @param artist Beatmap artist
     * @param artistUnicode Beatmap artist in Unicode
     * @param creator Beatmap creator
     * @param approved Rank status (-2 = graveyard -1 = WIP 0 = pending 1 = ranked 2 = approved 3 = qualified 4 = loved)
     * @param modes Beatmap Game Modes (1 = STD, 2 = taiko, 3 = CTB, 4 = mania)
     * @param order Sorting ID (popularity?)
     * @param playCount Beatmap Play Count
     * @param favouriteCount Beatmap Favourite Count
     * @param lastUpdate Beatmap Last Update Date
     */
    @Serializable
    data class SayobotBeatmapSetInfo(
        @SerialName("sid") val sid: Int,
        @SerialName("title") val title: String,
        @SerialName("titleU") val titleUnicode: String,
        @SerialName("artist") val artist: String,
        @SerialName("artistU") val artistUnicode: String,
        @SerialName("creator") val creator: String,
        @SerialName("approved") val approved: Int,
        @SerialName("modes") val modes: Int,
        @SerialName("order") val order: Double,
        @SerialName("play_count") val playCount: Long,
        @SerialName("favourite_count") val favouriteCount: Int,
        @SerialName("lastupdate") val lastUpdate: Long
    )
}
