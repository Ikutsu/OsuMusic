package io.ikutsu.osumusic.profile.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import core.presentation.res.OMIcon
import core.presentation.res.omicon.Fruits
import core.presentation.res.omicon.Levelbadge
import core.presentation.res.omicon.Mania
import core.presentation.res.omicon.Standard
import core.presentation.res.omicon.Taiko
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import io.ikutsu.osumusic.core.presentation.component.OMButton
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeLarge
import io.ikutsu.osumusic.core.presentation.util.HSpacer
import io.ikutsu.osumusic.core.presentation.util.OM_regular
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.mapLevelToTierColour
import io.ikutsu.osumusic.core.presentation.util.withThousandSeparator
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.ic_userLock
import osumusic.composeapp.generated.resources.loginBackground
import kotlin.math.abs

enum class GameMode(val value: String, val icon: ImageVector) {
    STD("osu", OMIcon.Standard),
    TAIKO("taiko", OMIcon.Taiko),
    CTB("fruits", OMIcon.Fruits),
    MANIA("mania", OMIcon.Mania)
}

@Composable
fun LoginCard(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit
) {
    val hazeState = remember { HazeState() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(OM_ShapeLarge),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.loginBackground),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.haze(
                hazeState,
                HazeStyle(
                    blurRadius = 6.dp
                )
            )
        )
        Column(
            modifier = Modifier.matchParentSize().hazeChild(hazeState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_userLock),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            VSpacer(4.dp)
            Text(
                text = "Sign in to view your profile",
                fontFamily = OM_regular,
                fontSize = 16.sp,
                color = Color.White
            )
            VSpacer(12.dp)
            OMButton(
                text = "Sign in",
                onClick = onSignInClick
            )
        }
    }
}

@Composable
fun DetailCard(
    modifier: Modifier = Modifier,
    profileBackground: String,
    profileAvatar: String,
    countryCode: String,
    username: String,
    gameMode: GameMode,
    level: Int,
    progress: Int,
    globalRank: String,
    countryRank: String,
    playTime: String,
    pp: String,
    accuracy: String,
    medals: String,
    combo: String,
) {
    val hazeState = remember { HazeState() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(OM_ShapeLarge),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .matchParentSize()
                .clip(OM_ShapeLarge)
                .haze(
                    hazeState,
                    HazeStyle(
                        blurRadius = 4.dp
                    )
                ),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(profileBackground)
                .build(),
            contentDescription = "Profile Background",
            imageLoader = koinInject(),
            contentScale = ContentScale.FillHeight,
        )
        Row(
            modifier = Modifier.hazeChild(hazeState).fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().width(96.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(profileAvatar)
                        .build(),
                    contentDescription = "Profile Avatar",
                    imageLoader = koinInject(),
                    modifier = Modifier.aspectRatio(1f).fillMaxSize().clip(OM_ShapeLarge),
                )
                VSpacer(16.dp)
                StatsItem(
                    modifier = Modifier.fillMaxWidth(),
                    barColor = Color(0xFF8C66FF),
                    title = "Global",
                    content = "#${globalRank.withThousandSeparator()}",
                    titleSize = 16.sp,
                    contentSize = 16.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f)
            ) {
                UserInfo(
                    countryCode = countryCode,
                    username = username,
                    gameMode = gameMode
                )
                VSpacer(8.dp)
                LevelProgress(
                    level = level,
                    progress = progress
                )
                VSpacer(8.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatsItem(
                        modifier = Modifier.weight(1f),
                        barColor = Color(0xFF8C66FF),
                        title = "Country",
                        content = "#${countryRank.withThousandSeparator()}"
                    )
                    StatsItem(
                        modifier = Modifier.weight(1f),
                        barColor = Color(0xFF66CCFF),
                        title = "Play Time",
                        content = playTime,
                    )
                }
                VSpacer(8.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatsItem(
                        modifier = Modifier.weight(1f),
                        barColor = Color(0xFFF01818),
                        title = "PP",
                        content = pp.withThousandSeparator(),
                    )
                    StatsItem(
                        modifier = Modifier.weight(1f),
                        barColor = Color(0xFFB3D944),
                        title = "Acc",
                        content = accuracy,
                    )
                    StatsItem(
                        modifier = Modifier.weight(1f),
                        barColor = Color(0xFFB3D944),
                        title = "Medal",
                        content = medals,
                    )
                    StatsItem(
                        modifier = Modifier.weight(1f),
                        barColor = Color(0xFFB3D944),
                        title = "Combo",
                        content = combo,
                    )
                }
            }
        }
    }
}

@Composable
fun UserInfo(
    modifier: Modifier = Modifier,
    countryCode: String,
    username: String,
    gameMode: GameMode
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data("https://osu.ppy.sh/images/flags/$countryCode.png")
                .build(),
            contentDescription = "Profile Avatar",
            imageLoader = koinInject(),
            modifier = Modifier.height(24.dp)
        )
        VSpacer(6.dp)
        Text(
            text = username,
            fontFamily = OM_regular,
            fontSize = 24.sp,
            color = Color.White
        )
        VSpacer(6.dp)
        Image(
            imageVector = gameMode.icon,
            contentDescription = "Game Mode",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
fun LevelProgress(
    modifier: Modifier = Modifier,
    level: Int,
    progress: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            LevelProgressIndicator(
                progress = { progress / 100f }
            )
            Text(
                text = "$progress%",
                fontFamily = OM_regular,
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.End),
                style = TextStyle(
                    lineHeightStyle = LineHeightStyle(
                        trim = LineHeightStyle.Trim.Both,
                        alignment = LineHeightStyle.Alignment.Center
                    )
                )
            )
        }
        HSpacer(6.dp)
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(mapLevelToTierColour(level), blendMode = BlendMode.SrcAtop)
                        }
                    },
                imageVector = OMIcon.Levelbadge,
                contentDescription = "Level Badge",
            )
            Text(
                text = level.toString(),
                fontFamily = OM_regular,
                fontSize = 13.sp,
                color = Color.White,
                modifier = Modifier.offset(0.dp, (-1).dp),
            )
        }
    }
}

@Composable
private fun LevelProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
) {
    val coercedProgress = { progress().coerceIn(0f, 1f) }
    Canvas(
        modifier = modifier.fillMaxWidth().height(3.dp),
    ) {
        val strokeWidth = size.height
        drawIndicator(
            color = Color(0xFF1C1719),
            startAt = 0f,
            endAt = 1f,
            strokeWidth = strokeWidth
        )
        drawIndicator(
            color = Color(0xFFFF66AB),
            startAt = 0f,
            endAt = coercedProgress(),
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawIndicator(
    color: Color,
    startAt: Float,
    endAt: Float,
    strokeWidth: Float,
) {
    val width = size.width
    val height = size.height

    val yOffset = height / 2

    val lineStart = width * startAt
    val lineEnd = width * endAt

    val strokeCapOffset = strokeWidth / 2
    val coerceRange = strokeCapOffset..(width - strokeCapOffset)
    val adjustedBarStart = lineStart.coerceIn(coerceRange)
    val adjustedBarEnd = lineEnd.coerceIn(coerceRange)

    if (abs(endAt - startAt) > 0) {
        // Progress line
        drawLine(
            color,
            Offset(adjustedBarStart, yOffset),
            Offset(adjustedBarEnd, yOffset),
            strokeWidth,
            StrokeCap.Round,
        )
    }
}

@Composable
private fun StatsItem(
    modifier: Modifier = Modifier,
    barColor: Color,
    title: String,
    content: String,
    titleSize: TextUnit = 14.sp,
    contentSize: TextUnit = 14.sp
) {
    Column(
        modifier = modifier,
    ) {
        Canvas(
            modifier = Modifier.fillMaxWidth().height(3.dp),
            onDraw = {
                drawRoundRect(
                    color = barColor,
                    cornerRadius = CornerRadius(100f),
                )
            }
        )
        VSpacer(4.dp)
        Text(
            text = title,
            fontFamily = OM_regular,
            fontSize = titleSize,
            color = Color.White,
            style = TextStyle(
                lineHeightStyle = LineHeightStyle(
                    trim = LineHeightStyle.Trim.Both,
                    alignment = LineHeightStyle.Alignment.Center
                )
            )
        )
        Text(
            text = content,
            fontFamily = OM_regular,
            fontSize = contentSize,
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