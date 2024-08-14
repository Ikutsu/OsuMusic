package io.ikutsu.osumusic.search.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request for Sayobot Beatmap List Query
 * @param cmd Command
 * @param limit Number of results to return
 * @param offset Offset of the results
 * @param type Classification (1 = Hot, 2 = New, 3 = Packs, 4 = Search)
 * @param keyword Search keyword
 * @param subType Exact match (1 = title/titleU, 2 = artist/artistU, 4 = creator, 8 = version, 16 = tags, 32 = source)
 * @param mode Game Mode (1 = STD, 2 = taiko, 3 = CTB, 4 = mania)
 * @param class_ Rank status (1 = Ranked & Approved, 2 = Qualified, 4 = Loved, 8 = Pending & WIP, 16 = Graveyard)
 * @param genre Genre (1 = Any, 2 = Unspecified, 4 = Video Game, 8 = Anime, 16 = Rock, 32 = Pop, 64 = Other, 128 = Novelty, 256 = Hip Hop, 512 = None, 1024 = Electronic)
 * @param language Language (1 = Any, 2 = Other, 4 = English, 8 = Japanese, 16 = Chinese, 32 = Instrumental, 64 = Korean, 128 = French, 256 = German, 512 = Swedish, 1024 = Spanish, 2048 = Italian, 4096 = None)
 * @param stars Star Rating Range (0.0 to 1000.0)
 * @param ar Approach Rate Range (0.0 to 1000.0)
 * @param od Overall Difficulty Range (0.0 to 1000.0)
 * @param cs Circle Size Range (0.0 to 1000.0)
 * @param hp Health Points Range (0.0 to 1000.0)
 * @param length Length Range (0.0 to 1000.0)
 * @param bpm Beats Per Minute Range (0.0 to 1000.0)
 */
@Serializable
data class SayobotBeatmapListRequest(
    @SerialName("cmd") val cmd: String = "beatmaplist",
    @SerialName("limit") val limit: Int = 25,
    @SerialName("offset") val offset: Int = 0,
    @SerialName("type") val type: Int = Type.SEARCH.value,
    @SerialName("keyword") val keyword: String,
    @SerialName("subType") val subType: Int? = null,
    @SerialName("mode") val mode: Int? = null,
    @SerialName("class") val class_: Int? = null,
    @SerialName("genre") val genre: Int? = null,
    @SerialName("language") val language: Int? = null,
    @SerialName("stars") val stars: ClosedFloatingPointRange<Float>? = null,
    @SerialName("ar") val ar: ClosedFloatingPointRange<Float>? = null,
    @SerialName("od") val od: ClosedFloatingPointRange<Float>? = null,
    @SerialName("cs") val cs: ClosedFloatingPointRange<Float>? = null,
    @SerialName("hp") val hp: ClosedFloatingPointRange<Float>? = null,
    @SerialName("length") val length: ClosedFloatingPointRange<Float>? = null,
    @SerialName("bpm") val bpm: ClosedFloatingPointRange<Float>? = null,
): BeatmapListRequest {
    enum class Type(val value: Int) {
        HOT(1),
        NEW(2),
        PACKS(3),
        SEARCH(4),
    }
    enum class SubType(val value: Int) {
        TITLE(1),
        ARTIST(2),
        CREATOR(4),
        VERSION(8),
        TAGS(16),
        SOURCE(32),
    }
    enum class Mode(val value: Int) {
        STD(1),
        TAIKO(2),
        CTB(4),
        MANIA(8),
    }
    enum class Class(val value: Int) {
        RANKED(1), // And Approved
        QUALIFIED(2),
        LOVED(4),
        PENDING(8), // And WIP
        GRAVEYARD(16),
    }
    enum class Genre(val value: Int) {
        ANY(1),
        UNSPECIFIED(2),
        VIDEO_GAME(4),
        ANIME(8),
        ROCK(16),
        POP(32),
        OTHER(64),
        NOVELTY(128),
        HIP_HOP(256),
        NONE(512),
        ELECTRONIC(1024)
    }
    enum class Language(val value: Int) {
        ANY(1),
        OTHER(2),
        ENGLISH(4),
        JAPANESE(8),
        CHINESE(16),
        INSTRUMENTAL(32),
        KOREAN(64),
        FRENCH(128),
        GERMAN(256),
        SWEDISH(512),
        SPANISH(1024),
        ITALIAN(2048),
        NONE(4096)
    }
}