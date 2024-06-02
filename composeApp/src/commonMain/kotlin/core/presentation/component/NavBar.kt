package core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import core.presentation.theme.OM_ShapeFull
import core.presentation.theme.OM_ShapeMedium

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier.background(
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    1f to Color.Black.copy(0.7f)
                )
            )
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun RowScope.NavItem(
    modifier: Modifier = Modifier,
    select: Boolean,
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(OM_ShapeMedium)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = modifier.size(24.dp),
            tint = if (select) Color.White else Color.Gray
        )
        Column(
            modifier = Modifier
                .width(16.dp)
                .height(2.dp)
                .offset(y = 16.dp)
                .background(
                    color = if (select) Color.White else Color.Transparent,
                    shape = OM_ShapeFull
                )

        ) {

        }
    }
}