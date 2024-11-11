package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.presentation.res.OMIcon
import core.presentation.res.omicon.History
import core.presentation.res.omicon.Sparkles
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeLarge
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.sp

@Composable
fun FeatureComingCard(
    modifier: Modifier = Modifier.fillMaxWidth().height(224.dp)
) {
    Column(
        modifier = modifier.background(Color.Gray.copy(alpha = 0.1f), shape = OM_ShapeLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = OMIcon.Sparkles,
            contentDescription = "Sparkles",
            modifier = Modifier.size(36.dp)
        )
        VSpacer(16.dp)
        Text(
            text = "Feature coming soon!",
            fontFamily = OM_SemiBold,
            fontSize = 20.dp.sp
        )
    }
}

@Composable
fun NoHistoryCard(
    modifier: Modifier = Modifier.fillMaxWidth().height(224.dp)
) {
    Column(
        modifier = modifier.background(Color.Gray.copy(alpha = 0.1f), shape = OM_ShapeLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = OMIcon.History,
            contentDescription = "History",
            modifier = Modifier.size(36.dp)
        )
        VSpacer(16.dp)
        Text(
            text = "There is no history yet :(",
            fontFamily = OM_SemiBold,
            fontSize = 20.dp.sp
        )
    }
}