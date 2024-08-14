package io.ikutsu.osumusic.search.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from Sayobot Beatmap Set Detail Query
 * @param data List of SayobotBeatmapInfo
 * @param status Unknown
 */
@Serializable
data class SayobotBeatmapSetDetailResponse(
    @SerialName("status") val status: Int,
    @SerialName("data") val data: List<SayobotBeatmapInfo>
) {
    /**
     * Sayobot Beatmap Info
     * @param beatmapId Beatmap Id
     * @param beatmapSetId Beatmap Set Id
     * @param title Beatmap title
     * @param titleUnicode Beatmap title in Unicode
     * @param artist Beatmap artist
     * @param artistUnicode Beatmap artist in Unicode
     * @param creator Beatmap creator
     * @param source Beatmap source
     * @param version Beatmap difficulty name
     * @param submitted Beatmap submitted date
     * @param ranked Beatmap ranked date
     * @param star Beatmap star rating
     * @param cs Beatmap CS
     * @param ar Beatmap AR
     * @param hp Beatmap HP
     * @param od Beatmap OD
     * @param maxCombo Beatmap max combo
     * @param mode Beatmap Game Mode (1 = STD, 2 = taiko, 3 = CTB, 4 = mania)
     * @param aim Beatmap aim rating
     * @param speed Beatmap speed rating
     * @param pp Beatmap pp rating
     * @param ppAim Beatmap aim pp rating
     * @param bpm Beatmap BPM
     * @param length Beatmap length
     * @param playCount Beatmap Play Count
     * @param passCount Beatmap Pass Count
     * @param favouriteCount Beatmap Favourite Count
     */
    @Serializable
    data class SayobotBeatmapInfo(
        @SerialName("bid") val beatmapId: Int,
        @SerialName("sid") val beatmapSetId: Int,
        @SerialName("title") val title: String,
        @SerialName("titleU") val titleUnicode: String,
        @SerialName("artist") val artist: String,
        @SerialName("artistU") val artistUnicode: String,
        @SerialName("creator") val creator: String,
        @SerialName("source") val source: String,
        @SerialName("version") val version: String,
        @SerialName("submitted") val submitted: Long,
        @SerialName("ranked") val ranked: Long,
        @SerialName("star") val star: Double,
        @SerialName("CS") val cs: Double,
        @SerialName("AR") val ar: Double,
        @SerialName("HP") val hp: Double,
        @SerialName("OD") val od: Double,
        @SerialName("maxcombo") val maxCombo: Int,
        @SerialName("mode") val mode: Int,
        @SerialName("aim") val aim: Double,
        @SerialName("speed") val speed: Double,
        @SerialName("pp") val pp: Double,
        @SerialName("pp_aim") val ppAim: Double,
        @SerialName("BPM") val bpm: Double,
        @SerialName("length") val length: Int,
        @SerialName("playcount") val playCount: Int,
        @SerialName("passcount") val passCount: Int,
        @SerialName("favourite_count") val favouriteCount: Int,
        @SerialName("img") val img: String,
        @SerialName("video") val video: String
    )
}