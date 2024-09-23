package io.ikutsu.osumusic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import io.ikutsu.osumusic.core.presentation.component.NavBar
import io.ikutsu.osumusic.core.presentation.component.NavItem
import io.ikutsu.osumusic.core.presentation.theme.OM_Background
import io.ikutsu.osumusic.core.presentation.util.HSpacer
import io.ikutsu.osumusic.core.presentation.util.noRippleClickable
import io.ikutsu.osumusic.home.presentation.HomeScreen
import io.ikutsu.osumusic.player.presentation.PlayerViewModel
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
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    onPlayerBarClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .background(OM_Background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val searchViewModel: SearchViewModel = koinViewModel()
        val playerUiState = playerViewModel.uiState.collectAsStateWithLifecycle()
        val density = LocalDensity.current

        NavHost(
            startDestination = "home",
            navController = navController,
            enterTransition = {
                fadeIn(tween(300, 300))
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
            modifier = Modifier.align(Alignment.BottomCenter).background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.5f to Color.Black.copy(0.75f),
                        1f to Color.Black
                    )
                )
            ).padding(top = 32.dp)
        ) {
            AnimatedVisibility(
                visibleState = remember {
                    MutableTransitionState(initialState = false)
                }.apply { targetState = playerUiState.value.currentMusic != null },
                enter = slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { with(density) { 80.dp.roundToPx() } }
                ) + expandVertically( expandFrom = Alignment.Bottom ),
                exit = slideOutVertically(
                    animationSpec = tween(300),
                    targetOffsetY = { with(density) { 80.dp.roundToPx() } }
                ) + shrinkVertically( shrinkTowards = Alignment.Bottom )
            ) {
                PlayerBar(
                    state = playerUiState,
                    onForward = {
                        playerViewModel.onNextClick()
                    },
                    onBackward = {
                        playerViewModel.onPreviousClick()
                    },
                    onPlayPause = {
                        playerViewModel.onPlayPauseClick()
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
            }
            HSpacer(8.dp)
            NavBar {
                BottomNavigationItem.forEach { screen ->
                    NavItem(
                        select = currentDestination?.route == screen.route,
                        onClick = {
                            if (currentDestination?.route != screen.route) {
                                navController.navigate(screen.route)
                            }
                        },
                        icon = painterResource(screen.resource),
                        contentDescription = screen.route,
                    )
                }
            }
        }
    }
}