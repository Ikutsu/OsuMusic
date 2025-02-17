package io.ikutsu.osumusic.search.data.remote.api.sayobot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from Sayobot Beatmap Set Detail Query
 * @param data List of SayobotBeatmapSetInfo
 * @param status Unknown
 */
@Serializable
data class SayobotBeatmapSetDetailResponse(
    @SerialName("status") val status: Int,
    @SerialName("data") val data: SayobotBeatmapSetInfo
) {
    /**
     * Response from Sayobot Beatmap Set Detail Query
     * @param approved Approval status
     * @param approvedDate Approval date
     * @param artist Artist
     * @param artistUnicode Artist Unicode
     * @param bidData List of SayobotBeatmapInfo
     * @param bidsAmount Bids amount
     * @param bpm BPM
     * @param creator Creator
     * @param creatorId Creator ID
     * @param favouriteCount Favourite count
     * @param genre Genre
     * @param language Language
     * @param lastUpdate Last update
     * @param localUpdate Local update
     * @param preview Preview
     * @param beatmapSetId Beatmap set ID
     * @param source Source
     * @param storyboard Storyboard
     * @param tags Tags
     * @param title Title
     * @param titleUnicode Title Unicode
     * @param video Video
     */
    @Serializable
    data class SayobotBeatmapSetInfo(
        @SerialName("approved") val approved: Int,
        @SerialName("approved_date") val approvedDate: Long,
        @SerialName("artist") val artist: String,
        @SerialName("artistU") val artistUnicode: String,
        @SerialName("bid_data") val bidData: List<SayobotBeatmapInfo>,
        @SerialName("bids_amount") val bidsAmount: Int,
        @SerialName("bpm") val bpm: Float,
        @SerialName("creator") val creator: String,
        @SerialName("creator_id") val creatorId: Long,
        @SerialName("favourite_count") val favouriteCount: Long,
        @SerialName("genre") val genre: Int,
        @SerialName("language") val language: Int,
        @SerialName("last_update") val lastUpdate: Long,
        @SerialName("local_update") val localUpdate: Long,
        @SerialName("preview") val preview: Int,
        @SerialName("sid") val beatmapSetId: Long,
        @SerialName("source") val source: String,
        @SerialName("storyboard") val storyboard: Int,
        @SerialName("tags") val tags: String,
        @SerialName("title") val title: String,
        @SerialName("titleU") val titleUnicode: String,
        @SerialName("video") val video: Int
    ) {
        /**
         * Response from Sayobot Beatmap Set Detail Query
         * @param ar AR
         * @param cs CS
         * @param hp HP
         * @param od OD
         * @param aim Aim
         * @param audio Audio
         * @param bg Background
         * @param bid Beatmap ID
         * @param circles Circles
         * @param hit300Window Hit 300 Window
         * @param img Image
         * @param length Length
         * @param maxCombo Max Combo
         * @param mode Mode
         * @param passCount Pass count
         * @param playCount Play count
         * @param pp PP
         * @param ppAcc PP Acc
         * @param ppAim PP Aim
         * @param ppSpeed PP Speed
         * @param sliders Sliders
         * @param speed Speed
         * @param spinners Spinners
         * @param star Star
         * @param strainAim Strain Aim
         * @param strainSpeed Strain Speed
         * @param version Diff name
         */
        @Serializable
        data class SayobotBeatmapInfo(
            @SerialName("AR") val ar: Float,
            @SerialName("CS") val cs: Float,
            @SerialName("HP") val hp: Float,
            @SerialName("OD") val od: Float,
            @SerialName("aim") val aim: Float,
            @SerialName("audio") val audio: String,
            @SerialName("bg") val bg: String,
            @SerialName("bid") val bid: Long,
            @SerialName("circles") val circles: Int,
            @SerialName("hit300window") val hit300Window: Int,
            @SerialName("img") val img: String,
            @SerialName("length") val length: Int,
            @SerialName("maxcombo") val maxCombo: Int,
            @SerialName("mode") val mode: Int,
            @SerialName("passcount") val passCount: Long,
            @SerialName("playcount") val playCount: Long,
            @SerialName("pp") val pp: Float,
            @SerialName("pp_acc") val ppAcc: Float,
            @SerialName("pp_aim") val ppAim: Float,
            @SerialName("pp_speed") val ppSpeed: Float,
            @SerialName("sliders") val sliders: Int,
            @SerialName("speed") val speed: Float,
            @SerialName("spinners") val spinners: Int,
            @SerialName("star") val star: Float,
            @SerialName("strain_aim") val strainAim: String,
            @SerialName("strain_speed") val strainSpeed: String,
            @SerialName("version") val version: String
        )
    }
}