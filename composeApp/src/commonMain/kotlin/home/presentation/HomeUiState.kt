package io.ikutsu.osumusic.home.presentation

import io.ikutsu.osumusic.core.domain.BeatmapMetadata

data class HomeUiState(
    val recentPlayedList: List<BeatmapMetadata> = listOf()
)