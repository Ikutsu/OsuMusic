package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_Bold
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.getDiffColor
import org.koin.compose.koinInject


@Composable
fun SingleDiffBeatmap(
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
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(beatmapCover)
                .build(),
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
            DiffCircle(diff , 24)
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
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
        }
    }

}

@Composable
fun AllDiffBeatmap(
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
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(beatmapCover)
                .build(),
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
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                diffs.forEach {
                    DiffCircle(it, 18)
                }
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