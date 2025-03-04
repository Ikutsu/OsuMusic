package io.ikutsu.osumusic.setting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.ikutsu.osumusic.core.presentation.component.OMDropDownMenu
import io.ikutsu.osumusic.core.presentation.component.OMSwitch
import io.ikutsu.osumusic.core.presentation.component.TitleBackTopBar
import io.ikutsu.osumusic.core.presentation.theme.OM_Background
import io.ikutsu.osumusic.core.presentation.util.OM_regular
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.WSpacer
import io.ikutsu.osumusic.core.presentation.util.sp

@Composable
fun SettingScreenRoot(
    viewModel: SettingViewModel,
    onBackClick: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SettingScreen(
        state = uiState,
        onAction = { action ->
            when (action) {
                SettingAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun SettingScreen(
    state: State<SettingUiState>,
    onAction: (SettingAction) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OM_Background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleBackTopBar(
            title = "Setting",
            onBackClick = { onAction(SettingAction.OnBackClick) }
        )
        SettingGroup(
            title = "Appreance"
        ) {
            SettingSwitch(
                title = "Show metadata in original language",
                checked = state.value.showInOriginalLang,
                onCheckedChange = { onAction(SettingAction.SetShowInOriginal(it)) }
            )
        }
        SettingGroup(
            title = "Playback"
        ) {
            SettingDropDown(
                title = "Beatmap source",
                selectedItem = state.value.beatmapSourceOptions.indexOf(state.value.beatmapSource),
                items = state.value.beatmapSourceOptions.map { it.value },
                onItemSelected = { index ->
                    onAction(SettingAction.SetBeatmapSource(index))
                }
            )
        }
    }
}

@Composable
fun SettingGroup(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            fontFamily = OM_regular,
            fontSize = 24.dp.sp
        )
        VSpacer(8.dp)
        content()
    }
}

@Composable
fun <T> SettingDropDown(
    modifier: Modifier = Modifier,
    title: String,
    selectedItem: Int,
    items: List<T>,
    onItemSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp).fillMaxWidth(),
    ) {
        Text(
            text = title,
            fontFamily = OM_regular,
            fontSize = 16.dp.sp
        )
        VSpacer(4.dp)
        OMDropDownMenu(
            selectedItem = selectedItem,
            items = items,
            onItemSelected = onItemSelected
        )
    }
}

@Composable
fun SettingSwitch(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = OM_regular,
            fontSize = 16.dp.sp
        )
        WSpacer()
        OMSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}