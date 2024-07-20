package io.ikutsu.osumusic.core.presentation.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun OMTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides Color.White
    ) {
        content()
    }
}