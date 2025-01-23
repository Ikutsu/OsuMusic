package io.ikutsu.osumusic.search.presentation

import io.ikutsu.osumusic.core.domain.BeatmapMetadata

enum class SearchUiContent {
    HISTORY,
    RESULT
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchText: String = "",
    val displaySearchText: String = "Search history",
    val searchContent: SearchUiContent = SearchUiContent.HISTORY,
    val searchHistory: List<BeatmapMetadata> = listOf(),
    val searchResult: List<BeatmapMetadata> = listOf()
)