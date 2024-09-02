package io.ikutsu.osumusic.home.presentation

import io.ikutsu.osumusic.core.domain.DiffBeatmapState

data class HomeUiState(
    val recentPlayedList: List<DiffBeatmapState> = listOf()
)