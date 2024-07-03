package io.ikutsu.osumusic.player.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ikutsu.osumusic.core.presentation.component.ProgressIndicator
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_Bold
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.ic_backward
import osumusic.composeapp.generated.resources.ic_forward
import osumusic.composeapp.generated.resources.ic_pause
import osumusic.composeapp.generated.resources.ic_play

@Composable
fun PlayerBar(
    beatmapCover: String,
    title: String,
    artist: String,
    progress: Float,
    isPlaying: Boolean,
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
                .data(beatmapCover)
                .build(),
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
                            1f to Color.Black.copy(0.6f)
                        )
                    )
                )
            }
        )
        ProgressIndicator(
            progress = { progress },
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
                    text = title,
                    fontFamily = OM_Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    style = TextStyle(
                        lineHeightStyle = LineHeightStyle(
                            trim = LineHeightStyle.Trim.Both,
                            alignment = LineHeightStyle.Alignment.Center
                        )
                    )
                )
                Text(
                    text = artist,
                    fontFamily = OM_SemiBold,
                    fontSize = 12.sp,
                    color = Color.White,
                    style = TextStyle(
                        lineHeightStyle = LineHeightStyle(
                            trim = LineHeightStyle.Trim.Both,
                            alignment = LineHeightStyle.Alignment.Center
                        )
                    )
                )
            }
            Icon(
                painter = painterResource(Res.drawable.ic_backward),
                contentDescription = "Backward",
                tint = Color.White,
                modifier = Modifier.size(24.dp).clickable { onBackward() }
            )
            Icon(
                painter = if (isPlaying) painterResource(Res.drawable.ic_pause) else painterResource(
                    Res.drawable.ic_play
                ),
                contentDescription = "Forward",
                tint = Color.White,
                modifier = Modifier.size(36.dp).clickable { onPlayPause() }
            )
            Icon(
                painter = painterResource(Res.drawable.ic_forward),
                contentDescription = "Forward",
                tint = Color.White,
                modifier = Modifier.size(24.dp).clickable { onForward() }
            )
        }
    }
}