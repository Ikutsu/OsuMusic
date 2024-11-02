package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ikutsu.osumusic.core.presentation.theme.OM_Secondary
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.debouncedClickable

@Composable
fun OMIconButton(
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    painter: Painter,
    contentDescription: String,
    containerSize: Dp = 48.dp,
    contentSize: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .clip(OM_ShapeMedium)
            .background(OM_Secondary)
            .debouncedClickable(
                onClick = onClick,
                enabled = enabled
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(contentSize)
        )
    }
}

@Composable
fun OMIconButton(
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    vector: ImageVector,
    contentDescription: String,
    containerSize: Dp = 48.dp,
    contentSize: Dp = 24.dp,
    containerColor: Color = OM_Secondary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .clip(OM_ShapeMedium)
            .background(containerColor)
            .debouncedClickable(
                onClick = onClick,
                enabled = enabled
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = vector,
            contentDescription = contentDescription,
            modifier = Modifier.size(contentSize)
        )
    }
}