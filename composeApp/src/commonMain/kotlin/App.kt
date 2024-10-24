package io.ikutsu.osumusic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ikutsu.osumusic.core.presentation.component.ErrorSnackBar
import io.ikutsu.osumusic.core.presentation.theme.OMTheme
import io.ikutsu.osumusic.core.presentation.theme.OM_Background
import io.ikutsu.osumusic.core.presentation.util.bottomBarPadding
import io.ikutsu.osumusic.player.presentation.PlayerScreen
import io.ikutsu.osumusic.player.presentation.PlayerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val appNavController = rememberNavController()
    val mainNavController = rememberNavController()
    val playerViewModel: PlayerViewModel = koinViewModel()
    val playerUiState = playerViewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.background(OM_Background)) {
        MaterialTheme {
            OMTheme {
                Box {
                    NavHost(
                        startDestination = "main",
                        navController = appNavController,
                    ) {
                        composable("main") {
                            MainScreen(
                                navController = mainNavController,
                                playerViewModel = playerViewModel,
                                onPlayerBarClick = {
                                    appNavController.navigate("player")
                                }
                            )
                        }
                        composable(
                            route = "player",
                            enterTransition = {
                                slideIn(
                                    animationSpec = tween(300),
                                    initialOffset = { IntOffset(0, it.height) }
                                ) + fadeIn(tween(300))
                            },
                            popEnterTransition = {
                                fadeIn(tween(300))
                            },
                            exitTransition = {
                                fadeOut(tween(300))
                            },
                            popExitTransition = {
                                slideOut(
                                    animationSpec = tween(300),
                                    targetOffset = { IntOffset(0, it.height) }
                                ) + fadeOut(tween(300))
                            }
                        ) {
                            PlayerScreen(
                                viewModel = playerViewModel,
                                onBackClick = {
                                    appNavController.navigateUp()
                                }
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = playerUiState.value.isError,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.bottomBarPadding().align(Alignment.BottomCenter)
                    ) {
                        ErrorSnackBar(errorMessage = playerUiState.value.errorMessage ?: "Unknown error")
                    }
                }
            }
        }
    }
}