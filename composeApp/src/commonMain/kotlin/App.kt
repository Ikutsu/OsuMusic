import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import core.presentation.component.NavBar
import core.presentation.component.NavItem
import core.presentation.component.TitleTopBar
import core.presentation.theme.OM_Background
import org.jetbrains.compose.resources.painterResource
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.ic_home
import osumusic.composeapp.generated.resources.ic_player
import osumusic.composeapp.generated.resources.ic_search

@Composable
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TitleTopBar(
                    title = "Home",
                    showSetting = true,
                    onSettingClick = {

                    }
                )
            },
            bottomBar = {
                NavBar {
                    NavItem(
                        icon = painterResource(Res.drawable.ic_home),
                        contentDescription = "Home",
                        onClick = {

                        },
                        select = true
                    )
                    NavItem(
                        icon = painterResource(Res.drawable.ic_search),
                        contentDescription = "Search",
                        onClick = {

                        },
                        select = false
                    )
                    NavItem(
                        icon = painterResource(Res.drawable.ic_player),
                        contentDescription = "Profile",
                        onClick = {

                        },
                        select = false
                    )
                }
            },
            containerColor = OM_Background
        ) {

        }
    }
}