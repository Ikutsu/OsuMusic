package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import io.ikutsu.osumusic.core.presentation.provider.LocalAppearanceSetting
import io.ikutsu.osumusic.core.presentation.util.OM_Bold
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold

@Composable
inline fun SongInfoTexts(
    title: String,
    unicodeTitle: String,
    artist: String,
    unicodeArtist: String,
) {
    val appearanceSettings = LocalAppearanceSetting.current
    Text(
        text = if (appearanceSettings.showInOriginalLang) unicodeTitle.ifEmpty { title } else title,
        fontFamily = OM_Bold,
        fontSize = 16.sp,
        style = TextStyle(
            lineHeightStyle = LineHeightStyle(
                trim = LineHeightStyle.Trim.Both,
                alignment = LineHeightStyle.Alignment.Center
            )
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        text = if (appearanceSettings.showInOriginalLang) unicodeArtist.ifEmpty { artist } else artist,
        fontFamily = OM_SemiBold,
        fontSize = 12.sp,
        style = TextStyle(
            lineHeightStyle = LineHeightStyle(
                trim = LineHeightStyle.Trim.Both,
                alignment = LineHeightStyle.Alignment.Center
            )
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}