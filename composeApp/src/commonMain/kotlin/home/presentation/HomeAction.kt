package io.ikutsu.osumusic.home.presentation

import io.ikutsu.osumusic.core.domain.BeatmapMetadata

sealed interface HomeAction {
    data object OnSettingClick : HomeAction
    data class OnPlayHistoryClick(val beatmapMetadata: BeatmapMetadata) : HomeAction
}