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

val OMIcon.CircleSpinner: ImageVector
    get() {
        if (_circlespinner != null) {
            return _circlespinner!!
        }
        _circlespinner = Builder(name = "CircleSpinner", defaultWidth = 512.0.dp, defaultHeight =
                512.0.dp, viewportWidth = 512.0f, viewportHeight = 512.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(222.7f, 32.1f)
                    curveTo(227.7f, 49.0f, 218.1f, 66.9f, 201.2f, 71.9f)
                    curveTo(121.8f, 95.6f, 64.0f, 169.1f, 64.0f, 256.0f)
                    curveTo(64.0f, 362.0f, 150.0f, 448.0f, 256.0f, 448.0f)
                    curveTo(362.0f, 448.0f, 448.0f, 362.0f, 448.0f, 256.0f)
                    curveTo(448.0f, 169.1f, 390.2f, 95.6f, 310.9f, 71.9f)
                    curveTo(294.0f, 66.9f, 284.3f, 49.0f, 289.4f, 32.1f)
                    curveTo(294.5f, 15.2f, 312.3f, 5.5f, 329.2f, 10.6f)
                    curveTo(434.9f, 42.1f, 512.0f, 140.0f, 512.0f, 256.0f)
                    curveTo(512.0f, 397.4f, 397.4f, 512.0f, 256.0f, 512.0f)
                    curveTo(114.6f, 512.0f, 0.0f, 397.4f, 0.0f, 256.0f)
                    curveTo(0.0f, 140.0f, 77.1f, 42.1f, 182.9f, 10.6f)
                    curveTo(199.8f, 5.6f, 217.7f, 15.2f, 222.7f, 32.1f)
                    close()
                }
            }
        }
        .build()
        return _circlespinner!!
    }

private var _circlespinner: ImageVector? = null
