package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import io.ikutsu.osumusic.core.presentation.theme.OM_Primary
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeFull
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_Bold
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.OM_regular
import io.ikutsu.osumusic.core.presentation.util.WSpacer
import io.ikutsu.osumusic.core.presentation.util.sp
import org.koin.compose.koinInject

@Composable
fun PlaylistCardItem(
    onClick: () -> Unit,
    playListCover: String,
    playlistName: String,
    beatmapsCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(OM_ShapeMedium)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(playListCover)
                .build(),
            contentDescription = "Playlist cover",
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
                            0f to OM_Primary,
                            0.15f to OM_Primary,
                            1f to Color.Transparent
                        )
                    )
                )
            }
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = playlistName,
                fontFamily = OM_SemiBold,
                fontSize = 16.dp.sp,
                style = TextStyle(
                    lineHeightStyle = LineHeightStyle(
                        trim = LineHeightStyle.Trim.Both,
                        alignment = LineHeightStyle.Alignment.Center
                    )
                )
            )
            WSpacer()
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontFamily = OM_Bold)) {
                        append(beatmapsCount.toString())
                    }
                    withStyle(style = SpanStyle(fontFamily = OM_regular)) {
                        append(
                            if (beatmapsCount > 1) " beatmaps" else " beatmap"
                        )
                    }
                },
                fontSize = 12.dp.sp,
                modifier = Modifier
                    .clip(OM_ShapeFull)
                    .drawBehind {
                        drawRect(
                            color = Color.Black
                        )
                    }
                    .padding(start = 8.dp, end = 8.dp, bottom = 1.dp),
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