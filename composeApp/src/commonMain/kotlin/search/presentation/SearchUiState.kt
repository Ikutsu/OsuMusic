package io.ikutsu.osumusic.search.presentation

import io.ikutsu.osumusic.core.domain.AllDiffBeatmapState

enum class SearchUiContent {
    HISTORY,
    RESULT
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val textFieldValue: String = "",
    val searchContent: SearchUiContent = SearchUiContent.HISTORY,
    val searchHistory: List<AllDiffBeatmapState> = listOf(),
    val searchResult: List<AllDiffBeatmapState> = listOf()
)