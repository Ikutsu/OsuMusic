package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import core.presentation.res.omicon.ListAdd
import core.presentation.res.omicon.Waveform
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.HSpacer
import io.ikutsu.osumusic.core.presentation.util.OM_Bold
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.debouncedClickable
import io.ikutsu.osumusic.core.presentation.util.getDiffColor
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.loginBackground


@Composable
fun SingleDiffBeatmap(
    onClick: () -> Unit,
    beatmapCover: String,
    title: String,
    artist: String,
    diff: Float,
    multiDiff: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(OM_ShapeMedium)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(beatmapCover)
                .build(),
            placeholder = painterResource(Res.drawable.loginBackground),
            error = painterResource(Res.drawable.loginBackground),
            contentDescription = "Beatmap cover",
            imageLoader = koinInject(),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.copy(0.6f),
                            0.8f to Color.Black.copy(0.3f),
                            1f to Color.Transparent
                        )
                    )
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (multiDiff) {
                RainbowDiffCircle(24)
            } else {
                DiffCircle(diff, 24)
            }
            HSpacer(8.dp)
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                 Text(
                     text = title,
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
                     text = artist,
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
        }
    }
}

@Composable
fun AllDiffBeatmap(
    onClick: () -> Unit,
    beatmapCover: String,
    title: String,
    artist: String,
    diffs: List<Float>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(OM_ShapeMedium)
            .debouncedClickable(500) {
                onClick()
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(beatmapCover)
                .build(),
            placeholder = painterResource(Res.drawable.loginBackground),
            error = painterResource(Res.drawable.loginBackground),
            contentDescription = "Beatmap cover",
            imageLoader = koinInject(),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )

        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.copy(0.6f),
                            0.8f to Color.Black.copy(0.3f),
                            1f to Color.Transparent
                        )
                    )
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
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
                text = artist,
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
            VSpacer(4.dp)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                userScrollEnabled = false
            ) {
                items(diffs) {
                    DiffCircle(it, 18)
                }
            }
        }
    }
}

@Composable
fun SwipeAllDiffBeatmap(
    onClick: () -> Unit,
    onSwipeRelease: () -> Unit,
    beatmapCover: String,
    title: String,
    artist: String,
    diffs: List<Float>
) {
    OMSwipeBox(
        allowSwipeLeft = true,
        allowSwipeRight = false,
        onSwipeRelease = onSwipeRelease,
        maxOffset = 0.2f,
        triggerThreshold = 0.15f,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .align(Alignment.CenterVertically)
                    .clip(OM_ShapeMedium)
                    .background(Color(0xFFFFCC22)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = OMIcon.ListAdd,
                    contentDescription = "Add to queue",
                    modifier = Modifier.size(24.dp)
                )
                HSpacer(16.dp)
            }
        },
        upperContent = {
            AllDiffBeatmap(
                onClick = onClick,
                beatmapCover = beatmapCover,
                title = title,
                artist = artist,
                diffs = diffs
            )
        }
    )
}

@Composable
fun PlaylistQueueItem(
    onClick: () -> Unit,
    isPlaying: Boolean,
    beatmapCover: String,
    title: String,
    artist: String,
    diff: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(OM_ShapeMedium)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(beatmapCover)
                .build(),
            placeholder = painterResource(Res.drawable.loginBackground),
            error = painterResource(Res.drawable.loginBackground),
            contentDescription = "Beatmap cover",
            imageLoader = koinInject(),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.copy(0.6f),
                            0.8f to Color.Black.copy(0.3f),
                            1f to Color.Transparent
                        )
                    )
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
//            DiffCircle(diff , 24)
            RainbowDiffCircle(24)
            HSpacer(8.dp)
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontFamily = OM_Bold,
                    fontSize = 16.sp,
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
                    style = TextStyle(
                        lineHeightStyle = LineHeightStyle(
                            trim = LineHeightStyle.Trim.Both,
                            alignment = LineHeightStyle.Alignment.Center
                        )
                    )
                )
            }
            if (isPlaying) {
                HSpacer(8.dp)
                Icon(
                    imageVector = OMIcon.Waveform,
                    contentDescription = "Waveform",
                    modifier = Modifier.height(24.dp)
                )
                HSpacer(8.dp)
            }
        }
    }

}

@Composable
fun DiffCircle(
    diff: Float,
    size: Int
) {
    Canvas(
        modifier = Modifier
            .size(size.dp)
    ) {
        drawCircle(
            color = Color.White,
        )
        drawCircle(
            color = diff.getDiffColor(),
            radius = size.toFloat(),
            style = Stroke(
                (size/4).toFloat()
            )
        )
    }
}

@Composable
fun RainbowDiffCircle(
    size: Int
) {
    Canvas(
        modifier = Modifier
            .size(size.dp)
    ) {
        drawCircle(
            color = Color.White,
        )
        drawCircle(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF4290FB),
                    Color(0xFF4FC0FF),
                    Color(0xFF4FFFD5),
                    Color(0xFF7CFF4F),
                    Color(0xFFF6F05C),
                    Color(0xFFFF8068),
                    Color(0xFFFF4E6F),
                    Color(0xFFC645B8),
                    Color(0xFF6563DE),
                    Color(0xFF18158E)
                )
            ),
            radius = size.toFloat(),
            style = Stroke(
                (size/4).toFloat()
            )
        )
    }

}