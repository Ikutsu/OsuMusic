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

val OMIcon.History: ImageVector
    get() {
        if (_history != null) {
            return _history!!
        }
        _history = Builder(name = "History", defaultWidth = 512.0.dp, defaultHeight = 512.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f).apply {
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(75.0f, 75.0f)
                lineTo(41.0f, 41.0f)
                curveTo(25.9f, 25.9f, 0.0f, 36.6f, 0.0f, 57.9f)
                verticalLineTo(168.0f)
                curveTo(0.0f, 181.3f, 10.7f, 192.0f, 24.0f, 192.0f)
                horizontalLineTo(134.1f)
                curveTo(155.5f, 192.0f, 166.2f, 166.1f, 151.1f, 151.0f)
                lineTo(120.3f, 120.2f)
                curveTo(155.0f, 85.5f, 203.0f, 64.0f, 256.0f, 64.0f)
                curveTo(362.0f, 64.0f, 448.0f, 150.0f, 448.0f, 256.0f)
                curveTo(448.0f, 362.0f, 362.0f, 448.0f, 256.0f, 448.0f)
                curveTo(215.2f, 448.0f, 177.4f, 435.3f, 146.3f, 413.6f)
                curveTo(131.8f, 403.5f, 111.9f, 407.0f, 101.7f, 421.5f)
                curveTo(91.5f, 436.0f, 95.1f, 455.9f, 109.6f, 466.1f)
                curveTo(151.2f, 495.0f, 201.7f, 512.0f, 256.0f, 512.0f)
                curveTo(397.4f, 512.0f, 512.0f, 397.4f, 512.0f, 256.0f)
                curveTo(512.0f, 114.6f, 397.4f, 0.0f, 256.0f, 0.0f)
                curveTo(185.3f, 0.0f, 121.3f, 28.7f, 75.0f, 75.0f)
                close()
                moveTo(256.0f, 128.0f)
                curveTo(242.7f, 128.0f, 232.0f, 138.7f, 232.0f, 152.0f)
                verticalLineTo(256.0f)
                curveTo(232.0f, 262.4f, 234.5f, 268.5f, 239.0f, 273.0f)
                lineTo(311.0f, 345.0f)
                curveTo(320.4f, 354.4f, 335.6f, 354.4f, 344.9f, 345.0f)
                curveTo(354.2f, 335.6f, 354.3f, 320.4f, 344.9f, 311.1f)
                lineTo(279.9f, 246.1f)
                verticalLineTo(152.0f)
                curveTo(279.9f, 138.7f, 269.2f, 128.0f, 255.9f, 128.0f)
                horizontalLineTo(256.0f)
                close()
            }
        }
        .build()
        return _history!!
    }

private var _history: ImageVector? = null
