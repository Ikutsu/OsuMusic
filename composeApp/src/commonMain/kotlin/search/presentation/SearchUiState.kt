package io.ikutsu.osumusic.search.presentation

import io.ikutsu.osumusic.core.domain.BeatmapMetadata

enum class SearchUiContent {
    HISTORY,
    RESULT
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val displayContentTitle: String = "Search history",
    val searchContent: SearchUiContent = SearchUiContent.HISTORY,
    val searchHistory: List<BeatmapMetadata> = listOf(),
    val searchResult: List<BeatmapMetadata> = listOf()
)