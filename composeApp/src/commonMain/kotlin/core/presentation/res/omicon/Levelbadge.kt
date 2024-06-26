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

val OMIcon.Levelbadge: ImageVector
    get() {
        if (_levelbadge != null) {
            return _levelbadge!!
        }
        _levelbadge = Builder(name = "levelbadge", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(11.8484f, 1.2958f)
                    curveTo(12.6379f, 1.2848f, 13.4163f, 1.4835f, 14.1039f, 1.8717f)
                    lineTo(19.5269f, 4.991f)
                    curveTo(20.2041f, 5.3877f, 20.7648f, 5.9559f, 21.1524f, 6.6383f)
                    curveTo(21.54f, 7.3208f, 21.7407f, 8.0934f, 21.7344f, 8.8783f)
                    verticalLineTo(15.117f)
                    curveTo(21.7407f, 15.9018f, 21.54f, 16.6745f, 21.1524f, 17.3569f)
                    curveTo(20.7648f, 18.0394f, 20.2041f, 18.6076f, 19.5269f, 19.0042f)
                    lineTo(14.1039f, 22.1236f)
                    curveTo(13.4255f, 22.5153f, 12.6558f, 22.7216f, 11.8724f, 22.7216f)
                    curveTo(11.0889f, 22.7216f, 10.3193f, 22.5153f, 9.6408f, 22.1236f)
                    lineTo(4.2179f, 19.0042f)
                    curveTo(3.5317f, 18.6137f, 2.9614f, 18.0481f, 2.5652f, 17.3652f)
                    curveTo(2.1689f, 16.6823f, 1.9609f, 15.9065f, 1.9624f, 15.117f)
                    verticalLineTo(8.8783f)
                    curveTo(1.9609f, 8.0887f, 2.1689f, 7.3129f, 2.5652f, 6.63f)
                    curveTo(2.9614f, 5.9471f, 3.5317f, 5.3816f, 4.2179f, 4.991f)
                    lineTo(9.6408f, 1.8717f)
                    curveTo(10.3144f, 1.4917f, 11.0751f, 1.2932f, 11.8484f, 1.2958f)
                    close()
                    moveTo(11.8484f, 0.0f)
                    curveTo(10.8375f, -0.0034f, 9.8439f, 0.2616f, 8.969f, 0.7679f)
                    lineTo(3.5461f, 3.8873f)
                    curveTo(2.67f, 4.393f, 1.9427f, 5.1206f, 1.4372f, 5.9967f)
                    curveTo(0.9318f, 6.8729f, 0.666f, 7.8667f, 0.6666f, 8.8783f)
                    verticalLineTo(15.117f)
                    curveTo(0.666f, 16.1285f, 0.9318f, 17.1224f, 1.4372f, 17.9985f)
                    curveTo(1.9427f, 18.8747f, 2.67f, 19.6023f, 3.5461f, 20.108f)
                    lineTo(8.969f, 23.2274f)
                    curveTo(9.8444f, 23.7328f, 10.8375f, 23.9989f, 11.8484f, 23.9989f)
                    curveTo(12.8593f, 23.9989f, 13.8524f, 23.7328f, 14.7278f, 23.2274f)
                    lineTo(20.1507f, 20.108f)
                    curveTo(21.0267f, 19.6023f, 21.7541f, 18.8747f, 22.2595f, 17.9985f)
                    curveTo(22.765f, 17.1224f, 23.0308f, 16.1285f, 23.0302f, 15.117f)
                    verticalLineTo(8.8783f)
                    curveTo(23.0308f, 7.8667f, 22.765f, 6.8729f, 22.2595f, 5.9967f)
                    curveTo(21.7541f, 5.1206f, 21.0267f, 4.393f, 20.1507f, 3.8873f)
                    lineTo(14.7278f, 0.7679f)
                    curveTo(13.8529f, 0.2616f, 12.8592f, -0.0034f, 11.8484f, 0.0f)
                    close()
                }
            }
        }
        .build()
        return _levelbadge!!
    }

private var _levelbadge: ImageVector? = null
