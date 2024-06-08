package core.presentation.component

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import core.presentation.theme.OM_ShapeMedium
import core.presentation.util.getDiffColor
import org.jetbrains.compose.resources.Font
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.torusbold
import osumusic.composeapp.generated.resources.torussemibold

@Composable
fun SingleDiffBeatmap(
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
                .data("https://assets.ppy.sh/beatmaps/880938/covers/cover.jpg?1650682949=")
                .build(),
            contentDescription = "Beatmap cover",
            imageLoader = ImageLoader(LocalPlatformContext.current),
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
            DiffCircle(diff = diff)
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                 Text(
                     text = "Title",
                     fontFamily = FontFamily(Font(Res.font.torusbold)),
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
                     text = "Artist",
                     fontFamily = FontFamily(Font(Res.font.torussemibold)),
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
fun AllDiffBeatmap() {
    
}

@Composable
fun DiffCircle(
    diff: Float
) {
    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        drawCircle(
            color = Color.White,
        )
        drawCircle(
            color = diff.getDiffColor(),
            radius = 24f,
            style = Stroke(
                6f
            )
        )
    }
}