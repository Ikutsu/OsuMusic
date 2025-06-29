package io.ikutsu.osumusic.player.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import core.presentation.res.OMIcon
import core.presentation.res.omicon.Backward
import core.presentation.res.omicon.Error
import core.presentation.res.omicon.Forward
import core.presentation.res.omicon.Heart
import core.presentation.res.omicon.Heartsolid
import core.presentation.res.omicon.Pause
import core.presentation.res.omicon.Play
import core.presentation.res.omicon.Shuffle
import io.ikutsu.osumusic.core.domain.toBeatmapMetadata
import io.ikutsu.osumusic.core.player.OMPlayerState
import io.ikutsu.osumusic.core.presentation.component.LoadingSpinner
import io.ikutsu.osumusic.core.presentation.component.NoBackgroundLoadingSpinner
import io.ikutsu.osumusic.core.presentation.component.OMIconButton
import io.ikutsu.osumusic.core.presentation.component.OMSlider
import io.ikutsu.osumusic.core.presentation.component.OMTab
import io.ikutsu.osumusic.core.presentation.component.OMTabRow
import io.ikutsu.osumusic.core.presentation.component.PlayerQueueItem
import io.ikutsu.osumusic.core.presentation.provider.LocalAppearanceSetting
import io.ikutsu.osumusic.core.presentation.theme.OM_Background
import io.ikutsu.osumusic.core.presentation.theme.OM_Primary
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeFull
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeLarge
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_Bold
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.WSpacer
import io.ikutsu.osumusic.core.presentation.util.debouncedClickable
import io.ikutsu.osumusic.core.presentation.util.formatMilliseconds
import io.ikutsu.osumusic.core.presentation.util.noRippleClickable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.ic_prevCircle
import osumusic.composeapp.generated.resources.loginBackground

enum class DisplayOptionTab(val title: String) {
    Background("Background"),
    Video("Video"),
    Storyboard("Storyboard"),
}

// Reserve for navigation-compose 2.8.0
//@Serializable
//object Player

@Composable
fun PlayerScreenRoot(
    viewModel: PlayerViewModel,
    onBackClick: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    PlayerScreen(
        state = uiState,
        onAction = {
            when (it) {
                PlayerAction.onBackClick -> onBackClick()
                else -> viewModel.onAction(it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    state: State<PlayerUiState>,
    onAction: (PlayerAction) -> Unit
) {

    val sheetState = rememberStandardBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    val showSheetDim = remember { mutableStateOf(false) }
    val sheetDim by animateColorAsState(
        if (sheetState.targetValue == SheetValue.PartiallyExpanded) Color.Transparent else Color(0x99000000),
        finishedListener = {
            showSheetDim.value = sheetState.targetValue != SheetValue.PartiallyExpanded
        }
    )


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        containerColor = OM_Background,
        topBar = {
            Box { // Todo: Remove this when scaffold color problem is fixed issue: https://issuetracker.google.com/issues/353628438
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OM_Background) // Todo: Remove this when scaffold color problem is fixed issue: https://issuetracker.google.com/issues/353628438
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.Center
                ) {
                    OMIconButton(
                        onClick = { onAction(PlayerAction.onBackClick) },
                        painter = painterResource(Res.drawable.ic_prevCircle),
                        contentDescription = "Back",
                    )
                }
                if (sheetState.targetValue == SheetValue.Expanded || showSheetDim.value) {
                    Box( // Todo: Remove this when scaffold color problem is fixed issue: https://issuetracker.google.com/issues/353628438
                        modifier = Modifier
                            .matchParentSize()
                            .background(sheetDim)
                            .noRippleClickable {
                                if (sheetState.targetValue == SheetValue.Expanded) {
                                    scope.launch {
                                        sheetState.partialExpand()
                                    }
                                }
                            }
                    )

                }
            }
        },
        sheetContent = {
            PlayerBottomSheet(
                onQueueItemClick = { onAction(PlayerAction.onQueueItemClick(it)) },
                state = state,
                sheetState = sheetState,
                scope = scope
            )
        },
        sheetDragHandle = {
            Box(
                Modifier
                    .padding(top = 8.dp)
                    .size(64.dp, 4.dp)
                    .clip(OM_ShapeFull)
                    .background(Color(0xFFD9D9D9))
            )
        },
        sheetPeekHeight = 64.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
        sheetContainerColor = OM_Primary,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerContent(
                    beatmapBackground = state.value.currentMusic?.backgroundUrl ?: ""
                )
                PlayerInfo(
                    state = state,
                    onProgressChange = { onAction(PlayerAction.onProgressChange(it)) },
                    onProgressChangeFinished = { onAction(PlayerAction.onSeek) }
                )
                Spacer(modifier = Modifier.requiredHeight(24.dp))
                PlayerControl(
                    state = state,
                    onShuffle = { },
                    onBackward = { onAction(PlayerAction.onSkipPreviousClick) },
                    onPlayPause = { onAction(PlayerAction.onPlayPauseClick) },
                    onForward = { onAction(PlayerAction.onSkipNextClick) },
                    onLove = { },
                    isShuffle = false,
                    isLoading = state.value.playerState == OMPlayerState.Buffering,
                    isError = state.value.playerState == OMPlayerState.Error,
                    isPlaying = state.value.playerState == OMPlayerState.Playing,
                    isLoved = true
                )
                Spacer(modifier = Modifier.requiredHeight(24.dp))
            }
            if (sheetState.targetValue == SheetValue.Expanded || showSheetDim.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(sheetDim)
                        .noRippleClickable {
                            if (sheetState.targetValue == SheetValue.Expanded) {
                                scope.launch {
                                    sheetState.partialExpand()
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun ColumnScope.PlayerContent(
    beatmapBackground: String
) {
    val isImageLoading = remember { mutableStateOf(false) }
    OMTabRow(
        selectedTabIndex = 0,
        tabs = {
            DisplayOptionTab.entries.forEachIndexed { index, displayOptionTab ->
                OMTab(
                    text = displayOptionTab.title,
                    onClick = {
//                                    selectedTabIndex.value = index
                    }
                )
            }
        },
    )
    VSpacer(16.dp)
    Box(
        modifier = Modifier.fillMaxWidth().weight(1f),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(beatmapBackground)
                .build(),
            placeholder = painterResource(Res.drawable.loginBackground),
            error = painterResource(Res.drawable.loginBackground),
            onLoading = { isImageLoading.value = true },
            onSuccess = { isImageLoading.value = false },
            onError = { isImageLoading.value = false },
            contentDescription = "Beatmap Background",
            imageLoader = koinInject(),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth().clip(OM_ShapeLarge)
        )
        if (isImageLoading.value) {
            LoadingSpinner(size = 64.dp)
        }
    }
    VSpacer(16.dp)
}

@Composable
fun PlayerInfo(
    state: State<PlayerUiState>,
    onProgressChange: (Float) -> Unit,
    onProgressChangeFinished: (() -> Unit),
) {
    val appearanceSettings = LocalAppearanceSetting.current
    Text(
        text = if (appearanceSettings.showInOriginalLang) state.value.currentMusic?.unicodeTitle ?: "Unknown" else state.value.currentMusic?.title ?: "Unknown",
        fontFamily = OM_Bold,
        fontSize = 24.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        text = if (appearanceSettings.showInOriginalLang) state.value.currentMusic?.unicodeArtist ?: "Unknown" else state.value.currentMusic?.artist ?: "Unknown",
        fontFamily = OM_SemiBold,
        fontSize = 16.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.requiredHeight(24.dp))
    OMSlider(
        value = state.value.currentProgress,
        onValueChange = { onProgressChange(it) },
        onValueChangeFinished = { onProgressChangeFinished() },
        valueRange = 0f..1f,
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = state.value.currentProgressInLong.formatMilliseconds(),
            fontFamily = OM_SemiBold,
            fontSize = 16.sp
        )
        WSpacer()
        Text(
            text = state.value.duration.formatMilliseconds(),
            fontFamily = OM_SemiBold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PlayerControl(
    state: State<PlayerUiState>,
    onShuffle: () -> Unit,
    onBackward: () -> Unit,
    onPlayPause: () -> Unit,
    onForward: () -> Unit,
    onLove: () -> Unit,
    isShuffle: Boolean,
    isLoading: Boolean,
    isPlaying: Boolean,
    isError: Boolean,
    isLoved: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().requiredHeight(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(
            imageVector = OMIcon.Shuffle,
            contentDescription = "Shuffle",
            tint = if (isShuffle) Color.White else Color.Gray,
            modifier = Modifier.size(24.dp).noRippleClickable { onShuffle() }
        )
        Icon(
            imageVector = OMIcon.Backward,
            contentDescription = "Backward",
            modifier = Modifier.size(36.dp).noRippleClickable { onBackward() }
        )
        when {
            isLoading -> {
                NoBackgroundLoadingSpinner(size = 48.dp)
            }
            isError -> {
                Icon(
                    imageVector = OMIcon.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(48.dp)
                )
            }
            else -> {
                Icon(
                    imageVector = if (isPlaying) OMIcon.Pause else OMIcon.Play,
                    contentDescription = "Play",
                    modifier = Modifier.size(48.dp).noRippleClickable { onPlayPause() }
                )
            }
        }
        Icon(
            imageVector = OMIcon.Forward,
            contentDescription = "Forward",
            tint = if (state.value.playerQueue.lastOrNull() == state.value.currentMusic) Color.Gray else Color.White,
            modifier = Modifier.size(36.dp).noRippleClickable { onForward() }
        )
        Icon(
            imageVector = if (isLoved) OMIcon.Heartsolid else OMIcon.Heart,
            contentDescription = "Love",
            tint = if (isLoved) Color.Red else Color.White,
            modifier = Modifier.size(24.dp).noRippleClickable { onLove() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerBottomSheet(
    state: State<PlayerUiState>,
    onQueueItemClick: (Int) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope
) {
    val listState = rememberLazyListState()

    LaunchedEffect(sheetState.targetValue) {
        if (sheetState.targetValue == SheetValue.Expanded) {
            listState.scrollToItem(0)
        }
    }

    Column(
        modifier = Modifier.fillMaxHeight(0.6f).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VSpacer(8.dp)
        Column(
            modifier = Modifier
                .clip(OM_ShapeMedium)
                .debouncedClickable(ripple = false) {
                    scope.launch {
                        sheetState.expand()
                    }
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .wrapContentSize()
        ) {
            Text(
                text = "Queue",
                fontFamily = OM_SemiBold,
                fontSize = 20.sp
            )
        }
        VSpacer(8.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            itemsIndexed(state.value.playerQueue) { index, item ->
                PlayerQueueItem(
                    onClick = { onQueueItemClick(index) },
                    isPlaying = item == state.value.currentMusic,
                    beatmapMetadata = item.toBeatmapMetadata()
                )
            }
        }
    }
}