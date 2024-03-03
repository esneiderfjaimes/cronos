package com.nei.cronos.core.designsystem.utils

import androidx.compose.foundation.background
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.transparentBackground(
    color: Color,
    elevation: Dp,
    shape: Shape = RectangleShape,
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color = DefaultShadowColor,
    spotColor: Color = DefaultShadowColor,
) = this
    .background(color, shape = shape)
    .transparentShadow(
        elevation = elevation,
        shape = shape,
        clip = clip,
        ambientColor = ambientColor,
        spotColor = spotColor
    )

@Stable
fun Modifier.transparentShadow(
    elevation: Dp,
    shape: Shape = RectangleShape,
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color = DefaultShadowColor,
    spotColor: Color = DefaultShadowColor,
) = if (elevation > 0.dp || clip) {
    drawWithCache {
        // Naive cache setup similar to foundation's Background.
        val path = Path()
        var lastSize: Size? = null

        fun updatePathIfNeeded() {
            if (size != lastSize) {
                path.reset()
                path.addOutline(
                    shape.createOutline(size, layoutDirection, this)
                )
                lastSize = size
            }
        }

        onDrawWithContent {
            updatePathIfNeeded()
            clipPath(path, ClipOp.Difference) {
                this@onDrawWithContent.drawContent()
            }
        }
    }.shadow(
        elevation,
        shape,
        clip = clip,
        ambientColor = ambientColor,
        spotColor = spotColor
    )
} else {
    this
}
