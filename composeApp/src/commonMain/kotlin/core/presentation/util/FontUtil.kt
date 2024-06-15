package io.ikutsu.osumusic.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.torusbold
import osumusic.composeapp.generated.resources.torusregular
import osumusic.composeapp.generated.resources.torussemibold

val OM_regular: FontFamily
    @Composable
    get() = FontFamily(Font(Res.font.torusregular))
val OM_SemiBold: FontFamily
    @Composable
    get() = FontFamily(Font(Res.font.torussemibold))

val OM_Bold: FontFamily
    @Composable
    get() = FontFamily(Font(Res.font.torusbold))