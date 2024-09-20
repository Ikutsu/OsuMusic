package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import core.presentation.res.OMIcon
import core.presentation.res.omicon.Error
import io.ikutsu.osumusic.core.presentation.theme.OM_Error
import io.ikutsu.osumusic.core.presentation.theme.OM_NotificationBackground
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.sp

@Composable
fun ErrorSnackBar(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max).clip(OM_ShapeMedium).background(OM_NotificationBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxHeight().width(40.dp).background(OM_Error),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = OMIcon.Error,
                contentDescription = "Error",
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = errorMessage,
            fontFamily = OM_SemiBold,
            fontSize = 14.dp.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}