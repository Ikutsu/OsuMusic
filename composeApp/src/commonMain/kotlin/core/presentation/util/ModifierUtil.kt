package io.ikutsu.osumusic.core.presentation.util

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

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

fun Modifier.debouncedClickable(
    delay: Long = 300,
    enabled: Boolean = true,
    ripple: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val localIndication = LocalIndication.current
    var clicked by remember {
        mutableStateOf(!enabled)
    }
    LaunchedEffect(key1 = clicked, block = {
        if (clicked) {
            delay(delay)
            clicked = !clicked
        }
    })

    clickable(
        enabled = if (enabled) !clicked else false,
        indication = if (ripple) localIndication else null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        clicked = !clicked
        onClick()
    }
}

inline fun Modifier.bottomBarPadding(): Modifier = composed {
    padding(bottom = 144.dp).navigationBarsPadding()
}