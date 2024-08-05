package io.ikutsu.osumusic.core.presentation.theme

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun OMTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides Color.White,
        LocalTextSelectionColors provides textSelectionColors
    ) {
        content()
    }
}

val textSelectionColors = TextSelectionColors(
    handleColor = OM_BrandColor,
    backgroundColor = OM_BrandColor.copy(alpha = 0.5f)
)