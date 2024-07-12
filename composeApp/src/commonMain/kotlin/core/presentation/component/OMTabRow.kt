package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFold
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import io.ikutsu.osumusic.core.presentation.theme.OM_Primary
import io.ikutsu.osumusic.core.presentation.theme.OM_Secondary
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.HSpacer
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold

private typealias TabPosition = Dp

@Composable
fun OMTabRow(
    selectedTabIndex: Int,
    tabs: @Composable () -> Unit,
    spacerWidth: Dp = 4.dp,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(OM_ShapeMedium)
            .background(OM_Primary)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = Color.Transparent,
        contentColor = Color.Transparent
    ) {
        SubcomposeLayout(
            modifier = Modifier.fillMaxWidth()
        ) { constraints ->
            val tabMeasurables = subcompose(2, tabs)
            val tabCount = tabMeasurables.size

            val spacerCount = tabCount - 1
            val spacerMeasurables = subcompose(1) {
                repeat(spacerCount) {
                    HSpacer(spacerWidth)
                }
            }

            val spacerPlaceables = spacerMeasurables.fastMap {
                it.measure(constraints.copy(minWidth = 0, minHeight = 0))
            }
            val spacerX = spacerPlaceables.first().width

            val tabWidth = (constraints.maxWidth - (spacerX * spacerCount)) / tabCount
            val tabHeight = tabMeasurables.fastFold(0) { max, curr ->
                maxOf(curr.maxIntrinsicHeight(tabWidth), max)
            }
            val tabPlaceables = tabMeasurables.fastMap {
                it.measure(
                    Constraints.fixed(
                        tabWidth,
                        tabHeight
                    )
                )
            }

            val tabPositions = List(tabCount) { index ->
                (tabWidth.toDp() * index) + (spacerX.toDp() * index)
            }

            layout(constraints.maxWidth, tabHeight) {
                subcompose(0) {
                    OMTabIndicator(selectedTabIndex, tabPositions)
                }.fastForEach {
                    it.measure(
                        Constraints.fixed(
                            tabWidth,
                            tabHeight
                        )
                    ).placeRelative(0, 0)
                }

                spacerPlaceables.fastForEachIndexed { index, placeable ->
                    placeable.placeRelative(
                        x = (index + 1) * tabWidth + (index * spacerX),
                        y = 0
                    )
                }

                tabPlaceables.fastForEachIndexed { index, placeable ->
                    placeable.placeRelative(
                        x = (index * tabWidth) + (index * spacerX),
                        y = 0
                    )
                }
            }
        }
    }
}

@Composable
fun OMTab(
    enable: Boolean = true,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(40.dp)
            .clip(OM_ShapeMedium)
            .selectable(
                selected = enable,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (enable) Color.White else Color.Gray,
            fontFamily = OM_SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun OMTabIndicator(
    currentTabIndex: Int,
    tabPositions: List<TabPosition>,
) {
    val offset by animateDpAsState(
        targetValue = tabPositions[currentTabIndex],
        animationSpec = tween(200)
    )

    Box(
        modifier = Modifier
            .offset(x = offset)
            .clip(OM_ShapeMedium)
            .background(OM_Secondary)
    )
}
