package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import core.presentation.res.OMIcon
import core.presentation.res.omicon.ListAdd
import core.presentation.res.omicon.Waveform
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.HSpacer
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.getDiffColor
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.loginBackground


@Composable
fun BeatmapItem(
    beatmapCover: String,
    title: String,
    unicodeTitle: String,
    artist: String,
    unicodeArtist: String,
    difficulty: Float,
    multiDiff: Boolean = false,
    trailingContent: @Composable (() -> Unit) = {},
    onClick: () -> Unit
) {
    BeatmapItemContainer(
        onClick = onClick,
        coverUrl = beatmapCover,
        height = 48.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (multiDiff) {
                RainbowDiffCircle(24)
            } else {
                DiffCircle(difficulty, 24)
            }
            HSpacer(8.dp)
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                SongInfoTexts(
                    title = title,
                    unicodeTitle = unicodeTitle,
                    artist = artist,
                    unicodeArtist = unicodeArtist
                )
            }
            trailingContent()
        }
    }
}

@Composable
fun BeatmapSetItem(
    beatmapCover: String,
    title: String,
    unicodeTitle: String,
    artist: String,
    unicodeArtist: String,
    difficulties: List<Float>,
    onClick: () -> Unit
) {
    BeatmapItemContainer(
        onClick = onClick,
        coverUrl = beatmapCover,
        height = 72.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            SongInfoTexts(
                title = title,
                unicodeTitle = unicodeTitle,
                artist = artist,
                unicodeArtist = unicodeArtist
            )
            VSpacer(4.dp)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                userScrollEnabled = false
            ) {
                items(difficulties) {
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
    unicodeTitle: String,
    artist: String,
    unicodeArtist: String,
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
            BeatmapSetItem(
                onClick = onClick,
                beatmapCover = beatmapCover,
                title = title,
                unicodeTitle = unicodeTitle,
                artist = artist,
                unicodeArtist = unicodeArtist,
                difficulties = diffs
            )
        }
    )
}

@Composable
fun PlayerQueueItem(
    beatmapCover: String,
    title: String,
    unicodeTitle: String,
    artist: String,
    unicodeArtist: String,
    difficulty: Float,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    BeatmapItem(
        beatmapCover = beatmapCover,
        title = title,
        unicodeTitle = unicodeTitle,
        artist = artist,
        unicodeArtist = unicodeArtist,
        difficulty = difficulty,
        onClick = onClick,
        trailingContent = {
            if (isPlaying) {
                Icon(
                    imageVector = OMIcon.Waveform,
                    contentDescription = "Waveform",
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    )
}

@Composable
fun BeatmapItemContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    coverUrl: String,
    height: Dp,
    shape: Shape = OM_ShapeMedium,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .clickable { onClick() }
    ) {
        BeatmapBackgroundImage(coverUrl = coverUrl)
        BeatmapGradientOverlay()

        content()
    }
}

@Composable
fun BeatmapBackgroundImage(
    coverUrl: String
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(coverUrl)
            .build(),
        placeholder = painterResource(Res.drawable.loginBackground),
        error = painterResource(Res.drawable.loginBackground),
        contentScale = ContentScale.FillWidth,
        contentDescription = "Beatmap cover",
        imageLoader = koinInject<ImageLoader>(),
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun BeatmapGradientOverlay(
    startColor: Color = Color.Black.copy(alpha = 0.6f),
    middleColor: Color = Color.Black.copy(alpha = 0.3f),
    endColor: Color = Color.Transparent
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawRect(
            brush = Brush.horizontalGradient(
                colorStops = arrayOf(
                    0f to startColor,
                    0.8f to middleColor,
                    1f to endColor
                )
            )
        )
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
                (size / 4).toFloat()
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
                (size / 4).toFloat()
            )
        )
    }

}