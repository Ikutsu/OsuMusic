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
import androidx.compose.runtime.State
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
fun SearchScreenRoot(
    viewModel: SearchViewModel,
    onSettingClick: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SearchAction.OnSettingClick -> onSettingClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun SearchScreen(
    state: State<SearchUiState>,
    onAction: (SearchAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
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
            onSettingClick = { onAction(SearchAction.OnSettingClick) }
        )
        SearchBar(
            textFieldValue = state.value.searchQuery,
            onTextFieldValueChange = {
                onAction(SearchAction.OnSearchQueryChange(it))
            },
            hintText = "type in keywords...",
            enableClearButton = true,
            onClearClick = {
                onAction(SearchAction.OnSearchQueryClear)
            },
            onSearchClick = {
                focusManager.clearFocus()
                onAction(SearchAction.OnSearchClick)
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
                            text = if (targetState == SearchUiContent.RESULT) state.value.displayContentTitle else "Search history",
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
                                        onClick = { onAction(SearchAction.OnSearchResultClick(it)) },
                                        onSwipeRelease = { onAction(SearchAction.OnSearchResultSwipe(it)) },
                                        beatmapCover = it.coverUrl,
                                        title = it.title,
                                        unicodeTitle = it.unicodeTitle,
                                        artist = it.artist,
                                        unicodeArtist = it.unicodeArtist,
                                        diffs = it.difficulties
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