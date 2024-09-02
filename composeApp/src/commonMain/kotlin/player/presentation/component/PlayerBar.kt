package io.ikutsu.osumusic.player.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import core.presentation.res.OMIcon
import core.presentation.res.omicon.Backward
import core.presentation.res.omicon.Forward
import core.presentation.res.omicon.Pause
import core.presentation.res.omicon.Play
import io.ikutsu.osumusic.core.presentation.component.NoBackgroundLoadingSpinner
import io.ikutsu.osumusic.core.presentation.component.ProgressIndicator
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_Bold
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
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
                Text(
                    text = state.value.currentMusic?.title ?: "Unknown",
                    fontFamily = OM_Bold,
                    fontSize = 16.sp,
                    style = TextStyle(
                        lineHeightStyle = LineHeightStyle(
                            trim = LineHeightStyle.Trim.Both,
                            alignment = LineHeightStyle.Alignment.Center
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = state.value.currentMusic?.artist ?: "Unknown",
                    fontFamily = OM_SemiBold,
                    fontSize = 12.sp,
                    style = TextStyle(
                        lineHeightStyle = LineHeightStyle(
                            trim = LineHeightStyle.Trim.Both,
                            alignment = LineHeightStyle.Alignment.Center
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = OMIcon.Backward,
                contentDescription = "Backward",
                modifier = Modifier.size(24.dp).noRippleClickable { onBackward() }
            )
            if (state.value.playerState == OMPlayerState.Buffering) {
                NoBackgroundLoadingSpinner(size = 36.dp)
            } else {
                Icon(
                    imageVector = if (state.value.playerState == OMPlayerState.Playing) OMIcon.Pause else OMIcon.Play,
                    contentDescription = "Forward",
                    modifier = Modifier.size(36.dp).noRippleClickable { onPlayPause() }
                )
            }
            Icon(
                imageVector = OMIcon.Forward,
                contentDescription = "Forward",
                modifier = Modifier.size(24.dp).noRippleClickable { onForward() }
            )
        }
    }
}