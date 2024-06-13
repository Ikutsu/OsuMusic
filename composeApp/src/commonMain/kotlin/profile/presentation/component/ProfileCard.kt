package profile.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.component.OMButton
import core.presentation.theme.OM_ShapeLarge
import core.presentation.util.OM_regular
import core.presentation.util.VSpacer
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import org.jetbrains.compose.resources.painterResource
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.ic_userLock
import osumusic.composeapp.generated.resources.loginBackground

@Composable
fun LoginCard(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit
) {
    val hazeState = remember { HazeState() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(194.dp)
            .clip(OM_ShapeLarge),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.loginBackground),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.haze(
                hazeState,
                HazeStyle(
                    blurRadius = 6.dp
                )
            )
        )
        Column(
            modifier = Modifier.matchParentSize().hazeChild(hazeState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_userLock),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            VSpacer(4.dp)
            Text(
                text = "Sign in to view your profile",
                fontFamily = OM_regular,
                fontSize = 16.sp,
                color = Color.White
            )
            VSpacer(12.dp)
            OMButton(
                text = "Sign in",
                onClick = onSignInClick
            )
        }
    }
}