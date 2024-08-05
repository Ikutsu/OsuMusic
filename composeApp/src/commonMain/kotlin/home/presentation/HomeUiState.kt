package io.ikutsu.osumusic.home.presentation

import io.ikutsu.osumusic.core.domain.SingleDiffBeatmapState

data class HomeUiState(
    val recentPlayedList: List<SingleDiffBeatmapState> = listOf(),
)