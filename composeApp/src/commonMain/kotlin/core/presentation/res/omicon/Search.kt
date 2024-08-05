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

val OMIcon.Search: ImageVector
    get() {
        if (_search != null) {
            return _search!!
        }
        _search = Builder(name = "Search", defaultWidth = 512.0.dp, defaultHeight = 512.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(375.954f, 219.977f)
                curveTo(375.954f, 254.397f, 364.781f, 286.192f, 345.959f, 311.989f)
                lineTo(440.895f, 407.0f)
                curveTo(450.268f, 416.373f, 450.268f, 431.596f, 440.895f, 440.97f)
                curveTo(431.521f, 450.343f, 416.298f, 450.343f, 406.925f, 440.97f)
                lineTo(311.989f, 345.959f)
                curveTo(286.192f, 364.856f, 254.397f, 375.954f, 219.977f, 375.954f)
                curveTo(133.815f, 375.954f, 64.0f, 306.14f, 64.0f, 219.977f)
                curveTo(64.0f, 133.815f, 133.815f, 64.0f, 219.977f, 64.0f)
                curveTo(306.14f, 64.0f, 375.954f, 133.815f, 375.954f, 219.977f)
                close()
                moveTo(219.977f, 327.961f)
                curveTo(234.158f, 327.961f, 248.2f, 325.168f, 261.301f, 319.742f)
                curveTo(274.402f, 314.315f, 286.306f, 306.361f, 296.333f, 296.333f)
                curveTo(306.361f, 286.306f, 314.315f, 274.402f, 319.742f, 261.301f)
                curveTo(325.168f, 248.2f, 327.961f, 234.158f, 327.961f, 219.977f)
                curveTo(327.961f, 205.796f, 325.168f, 191.755f, 319.742f, 178.653f)
                curveTo(314.315f, 165.552f, 306.361f, 153.648f, 296.333f, 143.621f)
                curveTo(286.306f, 133.594f, 274.402f, 125.639f, 261.301f, 120.213f)
                curveTo(248.2f, 114.786f, 234.158f, 111.993f, 219.977f, 111.993f)
                curveTo(205.796f, 111.993f, 191.755f, 114.786f, 178.653f, 120.213f)
                curveTo(165.552f, 125.639f, 153.648f, 133.594f, 143.621f, 143.621f)
                curveTo(133.594f, 153.648f, 125.639f, 165.552f, 120.213f, 178.653f)
                curveTo(114.786f, 191.755f, 111.993f, 205.796f, 111.993f, 219.977f)
                curveTo(111.993f, 234.158f, 114.786f, 248.2f, 120.213f, 261.301f)
                curveTo(125.639f, 274.402f, 133.594f, 286.306f, 143.621f, 296.333f)
                curveTo(153.648f, 306.361f, 165.552f, 314.315f, 178.653f, 319.742f)
                curveTo(191.755f, 325.168f, 205.796f, 327.961f, 219.977f, 327.961f)
                close()
            }
        }
        .build()
        return _search!!
    }

private var _search: ImageVector? = null
