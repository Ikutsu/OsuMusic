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

val OMIcon.Standard: ImageVector
    get() {
        if (_standard != null) {
            return _standard!!
        }
        _standard = Builder(name = "standard", defaultWidth = 500.0.dp, defaultHeight =
                500.0.dp, viewportWidth = 500.0f, viewportHeight = 500.0f).apply {
            group {
            }
            group {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(250.0f, 380.0f)
                    curveToRelative(-71.68f, 0.0f, -130.0f, -58.32f, -130.0f, -130.0f)
                    reflectiveCurveToRelative(58.32f, -130.0f, 130.0f, -130.0f)
                    reflectiveCurveToRelative(130.0f, 58.32f, 130.0f, 130.0f)
                    reflectiveCurveTo(321.68f, 380.0f, 250.0f, 380.0f)
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(250.0f, 140.0f)
                    arcToRelative(110.0f, 110.0f, 0.0f, true, true, -77.78f, 32.22f)
                    arcTo(109.25f, 109.25f, 0.0f, false, true, 250.0f, 140.0f)
                    moveToRelative(0.0f, -40.0f)
                    arcTo(150.0f, 150.0f, 0.0f, true, false, 400.0f, 250.0f)
                    arcTo(150.0f, 150.0f, 0.0f, false, false, 250.0f, 100.0f)
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
        return _standard!!
    }

private var _standard: ImageVector? = null
