package io.ikutsu.osumusic.search.presentation

import io.ikutsu.osumusic.core.domain.DiffBeatmapState

enum class SearchUiContent {
    HISTORY,
    RESULT
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val isUnicode: Boolean = false,
    val searchText: String = "",
    val displaySearchText: String = "Search history",
    val searchContent: SearchUiContent = SearchUiContent.HISTORY,
    val searchHistory: List<DiffBeatmapState> = listOf(),
    val searchResult: List<DiffBeatmapState> = listOf()
)