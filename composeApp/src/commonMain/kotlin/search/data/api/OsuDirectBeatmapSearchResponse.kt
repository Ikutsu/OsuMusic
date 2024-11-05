package io.ikutsu.osumusic.search.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsuDirectBeatmapSearchResponse(
    val beatmapSets: List<OsuDirectBeatmapSet>
): BeatmapSearchResponse {

    @Serializable
    data class OsuDirectBeatmapSet(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("title_unicode") val titleUnicode: String,
        @SerialName("artist") val artist: String,
        @SerialName("artist_unicode") val artistUnicode: String,
        @SerialName("creator") val creator: String,
        @SerialName("source") val source: String,
        @SerialName("tags") val tags: String,
        @SerialName("covers") val covers: Covers,
        @SerialName("favourite_count") val favouriteCount: Int,
        @SerialName("hype") val hype: Hype? = null,
        @SerialName("nsfw") val nsfw: Boolean,
        @SerialName("offset") val offset: Int,
        @SerialName("play_count") val playCount: Int,
        @SerialName("preview_url") val previewUrl: String,
        @SerialName("spotlight") val spotlight: Boolean,
        @SerialName("status") val status: String,
        @SerialName("track_id") val trackId: Int? = null,
        @SerialName("user_id") val userId: Int,
        @SerialName("video") val video: Boolean,
        @SerialName("bpm") val bpm: Double,
        @SerialName("can_be_hyped") val canBeHyped: Boolean,
        @SerialName("deleted_at") val deletedAt: String? = null,
        @SerialName("discussion_enabled") val discussionEnabled: Boolean,
        @SerialName("discussion_locked") val discussionLocked: Boolean,
        @SerialName("is_scoreable") val isScoreable: Boolean,
        @SerialName("last_updated") val lastUpdated: String,
        @SerialName("legacy_thread_url") val legacyThreadUrl: String,
        @SerialName("nominations_summary") val nominationsSummary: NominationsSummary,
        @SerialName("ranked") val ranked: Int,
        @SerialName("ranked_date") val rankedDate: String,
        @SerialName("storyboard") val storyboard: Boolean,
        @SerialName("submitted_date") val submittedDate: String,
        @SerialName("availability") val availability: Availability,
        @SerialName("has_favourited") val hasFavourited: Boolean,
        @SerialName("beatmaps") val beatmaps: List<Beatmap>,
        @SerialName("pack_tags") val packTags: List<String>,
        @SerialName("modes") val modes: List<Int>,
        @SerialName("last_checked") val lastChecked: String
    )

    @Serializable
    data class Covers(
        @SerialName("cover") val cover: String,
        @SerialName("cover@2x") val cover2x: String,
        @SerialName("card") val card: String,
        @SerialName("card@2x") val card2x: String,
        @SerialName("list") val list: String,
        @SerialName("list@2x") val list2x: String,
        @SerialName("slimcover") val slimCover: String,
        @SerialName("slimcover@2x") val slimCover2x: String
    )

    @Serializable
    data class Hype(
        @SerialName("current") val current: Int,
        @SerialName("required") val required: Int
    )

    @Serializable
    data class NominationsSummary(
        @SerialName("current") val current: Int,
        @SerialName("eligible_main_rulesets") val eligibleMainRulesets: List<String>,
        @SerialName("required_meta") val requiredMeta: RequiredMeta
    )

    @Serializable
    data class RequiredMeta(
        @SerialName("main_ruleset") val mainRuleset: Int,
        @SerialName("non_main_ruleset") val nonMainRuleset: Int
    )

    @Serializable
    data class Availability(
        @SerialName("download_disabled") val downloadDisabled: Boolean,
        @SerialName("more_information") val moreInformation: String? = null
    )

    @Serializable
    data class Beatmap(
        @SerialName("beatmapset_id") val beatmapsetId: Int,
        @SerialName("difficulty_rating") val difficultyRating: Double,
        @SerialName("id") val id: Int,
        @SerialName("mode") val mode: String,
        @SerialName("status") val status: String,
        @SerialName("total_length") val totalLength: Int,
        @SerialName("user_id") val userId: Int,
        @SerialName("version") val version: String,
        @SerialName("accuracy") val accuracy: Double,
        @SerialName("ar") val ar: Double,
        @SerialName("bpm") val bpm: Double,
        @SerialName("convert") val convert: Boolean,
        @SerialName("count_circles") val countCircles: Int,
        @SerialName("count_sliders") val countSliders: Int,
        @SerialName("count_spinners") val countSpinners: Int,
        @SerialName("cs") val cs: Double,
        @SerialName("deleted_at") val deletedAt: String? = null,
        @SerialName("drain") val drain: Double,
        @SerialName("hit_length") val hitLength: Int,
        @SerialName("is_scoreable") val isScoreable: Boolean,
        @SerialName("last_updated") val lastUpdated: String,
        @SerialName("mode_int") val modeInt: Int,
        @SerialName("passcount") val passCount: Int,
        @SerialName("playcount") val playCount: Int,
        @SerialName("ranked") val ranked: Int,
        @SerialName("url") val url: String,
        @SerialName("checksum") val checksum: String,
        @SerialName("max_combo") val maxCombo: Int
    )
}