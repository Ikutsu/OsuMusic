package io.ikutsu.osumusic.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ikutsu.osumusic.core.presentation.component.FeatureComingCard
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.sp
import io.ikutsu.osumusic.profile.presentation.component.LoginCard

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LoginCard(
            onSignInClick = { }
        )
        Text(
            text = "Recent Plays",
            fontFamily = OM_SemiBold,
            fontSize = 24.dp.sp
        )
        FeatureComingCard()
        Text(
            text = "Top Plays",
            fontFamily = OM_SemiBold,
            fontSize = 24.dp.sp
        )
        FeatureComingCard()
    }
}