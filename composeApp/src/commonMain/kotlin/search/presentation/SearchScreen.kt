package io.ikutsu.osumusic.search.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.presentation.res.OMIcon
import core.presentation.res.omicon.History
import io.ikutsu.osumusic.core.presentation.component.LoadingSpinner
import io.ikutsu.osumusic.core.presentation.component.SwipeAllDiffBeatmap
import io.ikutsu.osumusic.core.presentation.component.TitleTopBar
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.bottomBarPadding
import io.ikutsu.osumusic.core.presentation.util.sp
import io.ikutsu.osumusic.search.presentation.component.SearchBar

// Reserve for navigation-compose 2.8.0
//@Serializable
//object Search

@Composable
fun SearchScreen(
    viewmodel: SearchViewModel,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewmodel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleTopBar(
            title = "Search",
            showSetting = true,
            onSettingClick = onSettingClick
        )
        SearchBar(
            textFieldValue = state.value.searchText,
            onTextFieldValueChange = {
                viewmodel.onTextFieldChange(it)
            },
            hintText = "type in keywords...",
            enableClearButton = true,
            onClearClick = {
                viewmodel.onClearSearch()
            },
            onSearchClick = {
                focusManager.clearFocus()
                viewmodel.onSearch()
            }
        )
        AnimatedContent(
            targetState = state.value.isLoading,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                fadeIn(
                    tween(
                        200,
                        delayMillis = 200
                    )
                ) togetherWith fadeOut(
                    tween(
                        200,
                    )
                )
            }
        ) { targetState ->
            if (targetState) {
                Column(
                    modifier = Modifier.fillMaxSize().bottomBarPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingSpinner()
                }
            } else {
                AnimatedContent(
                    targetState = state.value.searchContent,
                    modifier = Modifier.fillMaxSize(),
                    transitionSpec = {
                        fadeIn(
                            tween(
                                200,
                                delayMillis = 200
                            )
                        ) togetherWith fadeOut(
                            tween(
                                200,
                            )
                        )
                    }
                ) { targetState ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = if (targetState == SearchUiContent.RESULT) state.value.displaySearchText else "Search history",
                            fontFamily = OM_SemiBold,
                            fontSize = 24.dp.sp
                        )
                        if (targetState == SearchUiContent.HISTORY && state.value.searchHistory.isEmpty()) {
                            Column(
                                modifier = Modifier.bottomBarPadding().fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = OMIcon.History,
                                    contentDescription = "History",
                                    modifier = Modifier.size(36.dp)
                                )
                                VSpacer(16.dp)
                                Text(
                                    text = "There is no history yet :(",
                                    fontFamily = OM_SemiBold,
                                    fontSize = 20.dp.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(
                                    if (targetState == SearchUiContent.RESULT) {
                                        state.value.searchResult
                                    } else {
                                        state.value.searchHistory
                                    }
                                ) {
                                    SwipeAllDiffBeatmap(
                                        onClick = { viewmodel.onSearchItemClick(it) },
                                        onSwipeRelease = { viewmodel.onSwipeRelease(it) },
                                        beatmapCover = it.coverUrl,
                                        title = it.title,
                                        titleUnicode = it.titleUnicode,
                                        artist = it.artist,
                                        artistUnicode = it.artistUnicode,
                                        diffs = it.diff
                                    )
                                }
                                item {
                                    Box(Modifier.bottomBarPadding())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}