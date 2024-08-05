package core.presentation.res.omicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import core.presentation.res.OMIcon

val OMIcon.Close: ImageVector
    get() {
        if (_close != null) {
            return _close!!
        }
        _close = Builder(name = "Close", defaultWidth = 512.0.dp, defaultHeight = 512.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(396.494f, 157.673f)
                curveTo(408.155f, 146.012f, 408.155f, 127.074f, 396.494f, 115.413f)
                curveTo(384.833f, 103.751f, 365.895f, 103.751f, 354.234f, 115.413f)
                lineTo(256.0f, 213.74f)
                lineTo(157.673f, 115.506f)
                curveTo(146.012f, 103.845f, 127.074f, 103.845f, 115.413f, 115.506f)
                curveTo(103.751f, 127.167f, 103.751f, 146.105f, 115.413f, 157.766f)
                lineTo(213.74f, 256.0f)
                lineTo(115.506f, 354.327f)
                curveTo(103.845f, 365.988f, 103.845f, 384.926f, 115.506f, 396.587f)
                curveTo(127.167f, 408.249f, 146.105f, 408.249f, 157.766f, 396.587f)
                lineTo(256.0f, 298.26f)
                lineTo(354.327f, 396.494f)
                curveTo(365.988f, 408.155f, 384.926f, 408.155f, 396.587f, 396.494f)
                curveTo(408.249f, 384.833f, 408.249f, 365.895f, 396.587f, 354.234f)
                lineTo(298.26f, 256.0f)
                lineTo(396.494f, 157.673f)
                close()
            }
        }
        .build()
        return _close!!
    }

private var _close: ImageVector? = null
