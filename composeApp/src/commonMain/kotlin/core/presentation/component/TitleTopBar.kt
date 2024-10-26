package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ikutsu.osumusic.core.presentation.util.HSpacer
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.WSpacer
import org.jetbrains.compose.resources.painterResource
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.ic_prevCircle
import osumusic.composeapp.generated.resources.ic_settings
import osumusic.composeapp.generated.resources.logo


@Composable
fun TitleTopBar(
    title: String,
    showSetting: Boolean,
    onSettingClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.padding(end = 8.dp).size(40.dp),
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = OM_SemiBold,
            fontSize = 32.sp
        )
        WSpacer()
        if (showSetting) {
            OMIconButton(
                onClick = onSettingClick,
                painter = painterResource(Res.drawable.ic_settings),
                contentDescription = "Settings",
                containerSize = 40.dp
            )
        }
    }
}

@Composable
fun TitleBackTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OMIconButton(
            onClick = {
                onBackClick()
            },
            painter = painterResource(Res.drawable.ic_prevCircle),
            contentDescription = "Back",
            containerSize = 40.dp
        )
        HSpacer(8.dp)
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = OM_SemiBold,
            fontSize = 32.sp
        )
    }
}