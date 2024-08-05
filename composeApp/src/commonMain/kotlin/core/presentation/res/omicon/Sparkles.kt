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

val OMIcon.Sparkles: ImageVector
    get() {
        if (_sparkles != null) {
            return _sparkles!!
        }
        _sparkles = Builder(name = "Sparkles", defaultWidth = 512.0.dp, defaultHeight = 512.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(327.5f, 85.2f)
                curveToRelative(-4.5f, 1.7f, -7.5f, 6.0f, -7.5f, 10.8f)
                reflectiveCurveToRelative(3.0f, 9.1f, 7.5f, 10.8f)
                lineTo(384.0f, 128.0f)
                lineToRelative(21.2f, 56.5f)
                curveToRelative(1.7f, 4.5f, 6.0f, 7.5f, 10.8f, 7.5f)
                reflectiveCurveToRelative(9.1f, -3.0f, 10.8f, -7.5f)
                lineTo(448.0f, 128.0f)
                lineToRelative(56.5f, -21.2f)
                curveToRelative(4.5f, -1.7f, 7.5f, -6.0f, 7.5f, -10.8f)
                reflectiveCurveToRelative(-3.0f, -9.1f, -7.5f, -10.8f)
                lineTo(448.0f, 64.0f)
                lineTo(426.8f, 7.5f)
                curveTo(425.1f, 3.0f, 420.8f, 0.0f, 416.0f, 0.0f)
                reflectiveCurveToRelative(-9.1f, 3.0f, -10.8f, 7.5f)
                lineTo(384.0f, 64.0f)
                lineTo(327.5f, 85.2f)
                close()
                moveTo(205.1f, 73.3f)
                curveToRelative(-2.6f, -5.7f, -8.3f, -9.3f, -14.5f, -9.3f)
                reflectiveCurveToRelative(-11.9f, 3.6f, -14.5f, 9.3f)
                lineTo(123.3f, 187.3f)
                lineTo(9.3f, 240.0f)
                curveTo(3.6f, 242.6f, 0.0f, 248.3f, 0.0f, 254.6f)
                reflectiveCurveToRelative(3.6f, 11.9f, 9.3f, 14.5f)
                lineToRelative(114.1f, 52.7f)
                lineTo(176.0f, 435.8f)
                curveToRelative(2.6f, 5.7f, 8.3f, 9.3f, 14.5f, 9.3f)
                reflectiveCurveToRelative(11.9f, -3.6f, 14.5f, -9.3f)
                lineToRelative(52.7f, -114.1f)
                lineToRelative(114.1f, -52.7f)
                curveToRelative(5.7f, -2.6f, 9.3f, -8.3f, 9.3f, -14.5f)
                reflectiveCurveToRelative(-3.6f, -11.9f, -9.3f, -14.5f)
                lineTo(257.8f, 187.4f)
                lineTo(205.1f, 73.3f)
                close()
                moveTo(384.0f, 384.0f)
                lineToRelative(-56.5f, 21.2f)
                curveToRelative(-4.5f, 1.7f, -7.5f, 6.0f, -7.5f, 10.8f)
                reflectiveCurveToRelative(3.0f, 9.1f, 7.5f, 10.8f)
                lineTo(384.0f, 448.0f)
                lineToRelative(21.2f, 56.5f)
                curveToRelative(1.7f, 4.5f, 6.0f, 7.5f, 10.8f, 7.5f)
                reflectiveCurveToRelative(9.1f, -3.0f, 10.8f, -7.5f)
                lineTo(448.0f, 448.0f)
                lineToRelative(56.5f, -21.2f)
                curveToRelative(4.5f, -1.7f, 7.5f, -6.0f, 7.5f, -10.8f)
                reflectiveCurveToRelative(-3.0f, -9.1f, -7.5f, -10.8f)
                lineTo(448.0f, 384.0f)
                lineToRelative(-21.2f, -56.5f)
                curveToRelative(-1.7f, -4.5f, -6.0f, -7.5f, -10.8f, -7.5f)
                reflectiveCurveToRelative(-9.1f, 3.0f, -10.8f, 7.5f)
                lineTo(384.0f, 384.0f)
                close()
            }
        }
        .build()
        return _sparkles!!
    }

private var _sparkles: ImageVector? = null
