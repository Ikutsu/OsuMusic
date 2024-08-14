package io.ikutsu.osumusic

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ikutsu.osumusic.core.presentation.theme.OMTheme
import io.ikutsu.osumusic.player.presentation.PlayerScreen

@Composable
fun App() {
    val appNavController = rememberNavController()
    val mainNavController = rememberNavController()

    MaterialTheme {
        OMTheme {
            NavHost(
                startDestination = "main",
                navController = appNavController,
            ) {
                composable("main") {
                    MainScreen(
                        navController = mainNavController,
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
                        onBackClick = {
                            appNavController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}