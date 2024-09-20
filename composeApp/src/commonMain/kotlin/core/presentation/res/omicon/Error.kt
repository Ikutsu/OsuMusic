package core.presentation.res.omicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import core.presentation.res.OMIcon

val OMIcon.Error: ImageVector
    get() {
        if (_error != null) {
            return _error!!
        }
        _error = Builder(name = "Error", defaultWidth = 48.0.dp, defaultHeight = 48.0.dp,
                viewportWidth = 48.0f, viewportHeight = 48.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(24.0f, 4.5f)
                    curveTo(29.1717f, 4.5f, 34.1316f, 6.5545f, 37.7886f, 10.2114f)
                    curveTo(41.4455f, 13.8684f, 43.5f, 18.8283f, 43.5f, 24.0f)
                    curveTo(43.5f, 29.1717f, 41.4455f, 34.1316f, 37.7886f, 37.7886f)
                    curveTo(34.1316f, 41.4455f, 29.1717f, 43.5f, 24.0f, 43.5f)
                    curveTo(18.8283f, 43.5f, 13.8684f, 41.4455f, 10.2114f, 37.7886f)
                    curveTo(6.5545f, 34.1316f, 4.5f, 29.1717f, 4.5f, 24.0f)
                    curveTo(4.5f, 18.8283f, 6.5545f, 13.8684f, 10.2114f, 10.2114f)
                    curveTo(13.8684f, 6.5545f, 18.8283f, 4.5f, 24.0f, 4.5f)
                    close()
                    moveTo(24.0f, 48.0f)
                    curveTo(30.3652f, 48.0f, 36.4697f, 45.4714f, 40.9706f, 40.9706f)
                    curveTo(45.4714f, 36.4697f, 48.0f, 30.3652f, 48.0f, 24.0f)
                    curveTo(48.0f, 17.6348f, 45.4714f, 11.5303f, 40.9706f, 7.0294f)
                    curveTo(36.4697f, 2.5286f, 30.3652f, 0.0f, 24.0f, 0.0f)
                    curveTo(17.6348f, 0.0f, 11.5303f, 2.5286f, 7.0294f, 7.0294f)
                    curveTo(2.5286f, 11.5303f, 0.0f, 17.6348f, 0.0f, 24.0f)
                    curveTo(0.0f, 30.3652f, 2.5286f, 36.4697f, 7.0294f, 40.9706f)
                    curveTo(11.5303f, 45.4714f, 17.6348f, 48.0f, 24.0f, 48.0f)
                    close()
                    moveTo(24.0f, 12.0f)
                    curveTo(22.7531f, 12.0f, 21.75f, 13.0031f, 21.75f, 14.25f)
                    verticalLineTo(24.75f)
                    curveTo(21.75f, 25.9969f, 22.7531f, 27.0f, 24.0f, 27.0f)
                    curveTo(25.2469f, 27.0f, 26.25f, 25.9969f, 26.25f, 24.75f)
                    verticalLineTo(14.25f)
                    curveTo(26.25f, 13.0031f, 25.2469f, 12.0f, 24.0f, 12.0f)
                    close()
                    moveTo(27.0f, 33.0f)
                    curveTo(27.0f, 32.2044f, 26.6839f, 31.4413f, 26.1213f, 30.8787f)
                    curveTo(25.5587f, 30.3161f, 24.7956f, 30.0f, 24.0f, 30.0f)
                    curveTo(23.2044f, 30.0f, 22.4413f, 30.3161f, 21.8787f, 30.8787f)
                    curveTo(21.3161f, 31.4413f, 21.0f, 32.2044f, 21.0f, 33.0f)
                    curveTo(21.0f, 33.7956f, 21.3161f, 34.5587f, 21.8787f, 35.1213f)
                    curveTo(22.4413f, 35.6839f, 23.2044f, 36.0f, 24.0f, 36.0f)
                    curveTo(24.7956f, 36.0f, 25.5587f, 35.6839f, 26.1213f, 35.1213f)
                    curveTo(26.6839f, 34.5587f, 27.0f, 33.7956f, 27.0f, 33.0f)
                    close()
                }
            }
        }
        .build()
        return _error!!
    }

private var _error: ImageVector? = null
