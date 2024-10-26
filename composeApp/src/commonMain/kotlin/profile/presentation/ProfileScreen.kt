package io.ikutsu.osumusic.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import io.ikutsu.osumusic.core.presentation.component.FeatureComingCard
import io.ikutsu.osumusic.core.presentation.component.TitleTopBar
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.bottomBarPadding
import io.ikutsu.osumusic.core.presentation.util.sp
import io.ikutsu.osumusic.profile.presentation.component.LoginCard

// Reserve for navigation-compose 2.8.0
//@Serializable
//object Profile

@Composable
fun ProfileScreen(
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val hazeState = remember { HazeState() }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleTopBar(
            title = "Profile",
            showSetting = true,
            onSettingClick = onSettingClick
        )
        Column(
            modifier = modifier.verticalScroll(scrollState).bottomBarPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box {
                LoginCard(
                    onSignInClick = { },
                    modifier = Modifier.haze(
                        hazeState,
                        HazeStyle(
                            blurRadius = 6.dp
                        )
                    )
                )
                FeatureComingCard(
                    modifier = Modifier.fillMaxWidth().matchParentSize().hazeChild(hazeState)
                )
            }
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
}