package io.ikutsu.osumusic.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

val Dp.sp @Composable get() = with (LocalDensity.current) { toSp() }