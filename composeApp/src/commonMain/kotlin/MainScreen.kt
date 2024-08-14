package io.ikutsu.osumusic

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.ikutsu.osumusic.core.presentation.component.NavBar
import io.ikutsu.osumusic.core.presentation.component.NavItem
import io.ikutsu.osumusic.core.presentation.theme.OM_Background
import io.ikutsu.osumusic.core.presentation.util.HSpacer
import io.ikutsu.osumusic.core.presentation.util.noRippleClickable
import io.ikutsu.osumusic.home.presentation.HomeScreen
import io.ikutsu.osumusic.player.presentation.component.PlayerBar
import io.ikutsu.osumusic.profile.presentation.ProfileScreen
import io.ikutsu.osumusic.search.presentation.SearchScreen
import io.ikutsu.osumusic.search.presentation.SearchViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import osumusic.composeapp.generated.resources.Res
import osumusic.composeapp.generated.resources.ic_home
import osumusic.composeapp.generated.resources.ic_player
import osumusic.composeapp.generated.resources.ic_search

private sealed class MainScreen(val route: String, val resource: DrawableResource) {
    data object HomeScreen : MainScreen("home", Res.drawable.ic_home)
    data object SearchScreen : MainScreen("search", Res.drawable.ic_search)
    data object ProfileScreen : MainScreen("profile", Res.drawable.ic_player)
}

private val BottomNavigationItem = listOf(
    MainScreen.HomeScreen,
    MainScreen.SearchScreen,
    MainScreen.ProfileScreen
)

// Reserve for navigation-compose 2.8.0
//@Serializable
//object Main

@Composable
fun MainScreen(
    onPlayerBarClick: () -> Unit
) {
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .background(OM_Background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val searchViewModel: SearchViewModel = koinViewModel()

        NavHost(
            startDestination = "home",
            navController = navController,
            enterTransition = {
                fadeIn(tween(300,300))
            },
            exitTransition = {
                fadeOut(tween(300))
            },
        ) {
            composable("home") {
                HomeScreen()
            }
            composable("search") {
                SearchScreen(
                    viewmodel = searchViewModel
                )
            }
            composable("profile") {
                ProfileScreen()
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            // TODO: Warp player bar with a AnimatedVisibility in the future
            PlayerBar(
                beatmapCover = "https://assets.ppy.sh/beatmaps/1205919/covers/raw.jpg",
                title = "UNION!!",
                artist = "765 MILLION ALLSTARS",
                progress = 0.5f,
                isPlaying = true,
                onForward = {

                },
                onBackward = {

                },
                onPlayPause = {

                },
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Transparent,
                            1f to Color.Black.copy(0.5f)
                        )
                    )
                ).padding(horizontal = 8.dp).noRippleClickable { onPlayerBarClick() }
            )
            HSpacer(8.dp)
            NavBar {
                BottomNavigationItem.forEach { screen ->
                    NavItem(
                        select = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route)
                        },
                        icon = painterResource(screen.resource),
                        contentDescription = screen.route,
                    )
                }
            }
        }
    }
}