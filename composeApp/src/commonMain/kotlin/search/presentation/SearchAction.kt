package io.ikutsu.osumusic.search.presentation

import io.ikutsu.osumusic.core.domain.BeatmapMetadata

sealed interface SearchAction {
    data object OnSettingClick : SearchAction
    data object OnSearchClick : SearchAction
    data object OnSearchQueryClear : SearchAction
    data class OnSearchQueryChange(val query: String) : SearchAction
    data class OnSearchResultClick(val beatmapMetadata: BeatmapMetadata) : SearchAction
    data class OnSearchResultSwipe(val beatmapMetadata: BeatmapMetadata) : SearchAction
}