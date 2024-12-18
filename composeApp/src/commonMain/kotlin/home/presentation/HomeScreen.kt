package io.ikutsu.osumusic.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.ikutsu.osumusic.core.presentation.component.FeatureComingCard
import io.ikutsu.osumusic.core.presentation.component.NoHistoryCard
import io.ikutsu.osumusic.core.presentation.component.SingleDiffBeatmap
import io.ikutsu.osumusic.core.presentation.component.TitleTopBar
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.bottomBarPadding
import io.ikutsu.osumusic.core.presentation.util.sp

// Reserve for navigation-compose 2.8.0
//@Serializable
//object Home

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.padding(horizontal = 16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleTopBar(
            title = "Home",
            showSetting = true,
            onSettingClick = onSettingClick
        )
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Recent played",
                    fontFamily = OM_SemiBold,
                    fontSize = 24.dp.sp
                )
                VSpacer(16.dp)
            }
            if (state.value.recentPlayedList.isEmpty()) {
                item {
                    NoHistoryCard()
                }
            }
            itemsIndexed(state.value.recentPlayedList.take(5)) { index, beatmap ->
                SingleDiffBeatmap(
                    onClick = {
                        viewModel.onPlayHistoryClicked(beatmap)
                    },
                    beatmapCover = beatmap.coverUrl,
                    title = beatmap.title,
                    artist = beatmap.artist,
                    diff = beatmap.diff.first(),
                    multiDiff = true
                )
                if (index < state.value.recentPlayedList.size - 1) {
                    VSpacer(8.dp)
                }
            }
            item {
                VSpacer(16.dp)
                Text(
                    text = "Playlist",
                    fontFamily = OM_SemiBold,
                    fontSize = 24.dp.sp
                )
                VSpacer(16.dp)
            }
            item {
                FeatureComingCard()
            }
            item {
                Box(Modifier.bottomBarPadding())
            }
        }
    }
}