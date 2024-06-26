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

val OMIcon.Mania: ImageVector
    get() {
        if (_mania != null) {
            return _mania!!
        }
        _mania = Builder(name = "mania", defaultWidth = 500.0.dp, defaultHeight = 500.0.dp,
                viewportWidth = 500.0f, viewportHeight = 500.0f).apply {
            group {
            }
            group {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(250.0f, 401.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, -25.0f, -25.0f)
                    verticalLineTo(124.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, 50.0f, 0.0f)
                    verticalLineTo(376.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, -25.0f, 25.0f)
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(170.0f, 330.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, -25.0f, -25.0f)
                    verticalLineTo(195.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, 50.0f, 0.0f)
                    verticalLineTo(305.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, -25.0f, 25.0f)
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(330.0f, 330.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, -25.0f, -25.0f)
                    verticalLineTo(195.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, 50.0f, 0.0f)
                    verticalLineTo(305.0f)
                    arcToRelative(25.0f, 25.0f, 0.0f, false, true, -25.0f, 25.0f)
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(250.0f, 55.0f)
                    curveToRelative(107.69f, 0.0f, 195.0f, 87.31f, 195.0f, 195.0f)
                    reflectiveCurveTo(357.69f, 445.0f, 250.0f, 445.0f)
                    reflectiveCurveTo(55.0f, 357.7f, 55.0f, 250.0f)
                    reflectiveCurveTo(142.3f, 55.0f, 250.0f, 55.0f)
                    moveToRelative(0.0f, -40.0f)
                    arcToRelative(235.06f, 235.06f, 0.0f, false, false, -91.49f, 451.52f)
                    arcToRelative(235.05f, 235.05f, 0.0f, false, false, 183.0f, -433.0f)
                    arcTo(233.59f, 233.59f, 0.0f, false, false, 250.0f, 15.0f)
                }
            }
        }
        .build()
        return _mania!!
    }

private var _mania: ImageVector? = null
