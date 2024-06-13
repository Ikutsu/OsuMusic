package core.presentation.util

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VSpacer(size: Dp) {
    Spacer(modifier = Modifier.height(size))
}

@Composable
fun HSpacer(size: Dp) {
    Spacer(modifier = Modifier.width(size))
}

@Composable
fun ColumnScope.WSpacer(size: Dp) {
    Spacer(modifier = Modifier.weight(1f))
}

@Composable
fun RowScope.WSpacer(size: Dp) {
    Spacer(modifier = Modifier.weight(1f))
}