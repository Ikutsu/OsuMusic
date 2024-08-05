package io.ikutsu.osumusic.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ikutsu.osumusic.core.presentation.component.AllDiffBeatmap
import io.ikutsu.osumusic.core.presentation.component.LoadingSpinner
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.sp
import io.ikutsu.osumusic.search.presentation.component.SearchBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Remove all the hardcoded logic when viewmodel is implemented

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val state = remember { mutableStateOf(SearchUiState()) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SearchBar(
            textFieldValue = state.value.textFieldValue,
            onTextFieldValueChange = { state.value = state.value.copy(textFieldValue = it) },
            hintText = "type in keywords...",
            enableClearButton = true,
            onClearClick = {
                state.value = state.value.copy(textFieldValue = "", searchContent = SearchUiContent.HISTORY)
            },
            onSearchClick = {
                scope.launch {
                    state.value = state.value.copy(isLoading = true, searchContent = SearchUiContent.RESULT)
                    delay(3000)
                    state.value = state.value.copy(isLoading = false)
                }
            }
        )
        if (state.value.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingSpinner()
            }
        } else {
            Text(
                text = if (state.value.searchContent == SearchUiContent.RESULT) {
                    "Search result for \"${state.value.textFieldValue}\""
                } else {
                    "Search history"
                },
                fontFamily = OM_SemiBold,
                fontSize = 24.dp.sp
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    if (state.value.searchContent == SearchUiContent.RESULT) {
                        state.value.searchResult
                    } else {
                        state.value.searchHistory
                    }
                ) {
                    AllDiffBeatmap(
                        onClick = { },
                        beatmapCover = it.coverUrl,
                        title = it.title,
                        artist = it.artist,
                        diffs = it.diff
                    )
                }
            }
        }
    }
}