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

val OMIcon.ListAdd: ImageVector
    get() {
        if (_listAdd != null) {
            return _listAdd!!
        }
        _listAdd = Builder(name = "ListAdd", defaultWidth = 512.0.dp, defaultHeight = 512.0.dp,
                viewportWidth = 512.0f, viewportHeight = 512.0f).apply {
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(53.333f, 122.667f)
                curveTo(53.333f, 109.052f, 64.333f, 98.052f, 77.949f, 98.052f)
                horizontalLineTo(356.923f)
                curveTo(370.538f, 98.052f, 381.538f, 109.052f, 381.538f, 122.667f)
                curveTo(381.538f, 136.283f, 370.538f, 147.283f, 356.923f, 147.283f)
                horizontalLineTo(77.949f)
                curveTo(64.333f, 147.283f, 53.333f, 136.283f, 53.333f, 122.667f)
                close()
            }
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(77.949f, 196.514f)
                curveTo(64.333f, 196.514f, 53.333f, 207.514f, 53.333f, 221.129f)
                curveTo(53.333f, 234.744f, 64.333f, 245.744f, 77.949f, 245.744f)
                horizontalLineTo(274.872f)
                curveTo(288.487f, 245.744f, 299.487f, 234.744f, 299.487f, 221.129f)
                curveTo(299.487f, 207.514f, 288.487f, 196.514f, 274.872f, 196.514f)
                horizontalLineTo(77.949f)
                close()
            }
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(77.949f, 294.975f)
                curveTo(64.333f, 294.975f, 53.333f, 305.975f, 53.333f, 319.59f)
                curveTo(53.333f, 333.206f, 64.333f, 344.206f, 77.949f, 344.206f)
                horizontalLineTo(176.41f)
                curveTo(190.026f, 344.206f, 201.026f, 333.206f, 201.026f, 319.59f)
                curveTo(201.026f, 305.975f, 190.026f, 294.975f, 176.41f, 294.975f)
                horizontalLineTo(77.949f)
                close()
            }
            path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(233.846f, 319.59f)
                curveTo(233.846f, 305.975f, 244.846f, 294.975f, 258.461f, 294.975f)
                horizontalLineTo(332.308f)
                verticalLineTo(221.129f)
                curveTo(332.308f, 207.514f, 343.308f, 196.514f, 356.923f, 196.514f)
                curveTo(370.538f, 196.514f, 381.538f, 207.514f, 381.538f, 221.129f)
                verticalLineTo(294.975f)
                horizontalLineTo(455.385f)
                curveTo(469.0f, 294.975f, 480.0f, 305.975f, 480.0f, 319.59f)
                curveTo(480.0f, 333.206f, 469.0f, 344.206f, 455.385f, 344.206f)
                horizontalLineTo(381.538f)
                verticalLineTo(418.052f)
                curveTo(381.538f, 431.667f, 370.538f, 442.667f, 356.923f, 442.667f)
                curveTo(343.308f, 442.667f, 332.308f, 431.667f, 332.308f, 418.052f)
                verticalLineTo(344.206f)
                horizontalLineTo(258.461f)
                curveTo(244.846f, 344.206f, 233.846f, 333.206f, 233.846f, 319.59f)
                close()
            }
        }
        .build()
        return _listAdd!!
    }

private var _listAdd: ImageVector? = null
