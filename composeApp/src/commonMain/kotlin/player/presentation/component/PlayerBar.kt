package io.ikutsu.osumusic.player.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import core.presentation.res.OMIcon
import core.presentation.res.omicon.Backward
import core.presentation.res.omicon.Error
import core.presentation.res.omicon.Forward
import core.presentation.res.omicon.Pause
import core.presentation.res.omicon.Play
import io.ikutsu.osumusic.core.presentation.component.NoBackgroundLoadingSpinner
import io.ikutsu.osumusic.core.presentation.component.ProgressIndicator
import io.ikutsu.osumusic.core.presentation.component.SongInfoTexts
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.noRippleClickable
import io.ikutsu.osumusic.player.player.OMPlayerState
import io.ikutsu.osumusic.player.presentation.PlayerUiState
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.loginBackground

@Composable
fun PlayerBar(
    state: State<PlayerUiState>,
    onBackward: () -> Unit,
    onForward: () -> Unit,
    onPlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth().height(64.dp).clip(OM_ShapeMedium)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(state.value.currentMusic?.coverUrl)
                .build(),
            placeholder = painterResource(Res.drawable.loginBackground),
            error = painterResource(Res.drawable.loginBackground),
            contentDescription = "Beatmap cover",
            imageLoader = koinInject(),
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillWidth
        )
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Transparent,
                            1f to Color.Black.copy(0.8f)
                        )
                    )
                )
            }
        )
        ProgressIndicator(
            progress = { state.value.currentProgress },
            height = 4.dp,
            trackColor = Color(0xFF673401),
            indicatorColor = Color(0xFFFFCC22),
            strokeCap = StrokeCap.Butt,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        )

        Row(
            modifier = Modifier.matchParentSize().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                SongInfoTexts(
                    title = state.value.currentMusic?.title ?: "Unknown",
                    unicodeTitle = state.value.currentMusic?.unicodeTitle ?: "Unknown",
                    artist = state.value.currentMusic?.artist ?: "Unknown",
                    unicodeArtist = state.value.currentMusic?.unicodeArtist ?: "Unknown",
                )
            }
            Icon(
                imageVector = OMIcon.Backward,
                contentDescription = "Backward",
                modifier = Modifier.size(24.dp).noRippleClickable { onBackward() }
            )
            when (state.value.playerState) {
                OMPlayerState.Buffering -> {
                    NoBackgroundLoadingSpinner(size = 36.dp)
                }
                OMPlayerState.Error -> {
                    Icon(
                        imageVector = OMIcon.Error,
                        contentDescription = "Error",
                        modifier = Modifier.size(36.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector = if (state.value.playerState == OMPlayerState.Playing) OMIcon.Pause else OMIcon.Play,
                        contentDescription = "Play/Pause",
                        modifier = Modifier.size(36.dp).noRippleClickable { onPlayPause() }
                    )
                }
            }
            Icon(
                imageVector = OMIcon.Forward,
                contentDescription = "Forward",
                modifier = Modifier.size(24.dp).noRippleClickable { onForward() }
            )
        }
    }
}