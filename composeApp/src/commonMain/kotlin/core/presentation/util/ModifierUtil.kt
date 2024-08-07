package io.ikutsu.osumusic.core.presentation.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

inline fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    noinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        enabled = enabled,
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )
}

inline fun Modifier.bottomBarPadding(): Modifier = composed {
    padding(bottom = 144.dp).navigationBarsPadding()
}