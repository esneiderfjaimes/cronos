/*
package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.patternDotBG(
    colorBackground: Color = Color(0xFF020518),
    shapeSize: Dp = 32.dp, // TODO FIX multiple shapes
    colorDot: Color = Color(0xFF2B2D3C),
    shadowColor: Color = colorBackground,
    dotSize: Dp = 2.dp,
    space: Dp = 25.dp
) = clip(RoundedCornerShape(shapeSize))
    .background(colorBackground)
    .drawBehind {
        val list = getPointMode(size.width, size.height, space.toPx())
        drawPoints(
            points = (list),
            pointMode = PointMode.Points,
            color = colorDot,
            cap = StrokeCap.Round,
            strokeWidth = dotSize.toPx(),
        )
        drawShadowBorder(
            colors = listOf(Color.Transparent, shadowColor),
            shapeSize = shapeSize,
        )
    }

fun Modifier.patternDotBrushBG(
    colorBackground: Color = Color(0xFF020518),
    shapeSize: Dp = 32.dp, // TODO FIX multiple shapes
    brushDot: Brush = Brush.linearGradient(
        listOf(Color.Blue, Color.Red),
    ),
    shadowColors: List<Color> = listOf(Color.Transparent, colorBackground),
    dotSize: Dp = 2.dp,
    space: Dp = 25.dp
) = clip(RoundedCornerShape(shapeSize))
    .background(colorBackground)
    .drawBehind {
        val list = getPointMode(size.width, size.height, space.toPx())
        drawPoints(
            points = (list),
            pointMode = PointMode.Points,
            brush = brushDot,
            cap = StrokeCap.Round,
            strokeWidth = dotSize.toPx(),
        )
        drawShadowBorder(
            colors = shadowColors,
            shapeSize = shapeSize,
        )
    }

fun DrawScope.drawShadowBorder(
    colors: List<Color>, shapeSize: Dp, border: Dp = 0.dp
) {
    val innerShape = shapeSize - border
    val arcSize = (innerShape * 2).toPx()
    val radiusShape = (innerShape / 2).toPx()
    val innerShapePx = (innerShape).toPx()
    val borderPx = border.toPx()
    val sizeArc = Size(arcSize, arcSize)
    drawArc(
        brush = Brush.radialGradient(
            colors = colors,
            center = Offset(
                x = innerShapePx + borderPx,
                y = innerShapePx + borderPx
            ),
            radius = innerShapePx
        ),
        size = sizeArc,
        topLeft = Offset(
            x = borderPx,
            y = borderPx
        ),
        startAngle = 180f,
        sweepAngle = 90f,
        useCenter = true,
    )

    drawLine(
        brush = Brush.verticalGradient(
            colors = colors,
            startY = innerShapePx + borderPx,
            endY = borderPx
        ),
        start = Offset(
            x = innerShapePx + borderPx,
            y = radiusShape + borderPx
        ),
        end = Offset(
            x = size.width - innerShapePx - borderPx,
            y = radiusShape + borderPx
        ),
        strokeWidth = innerShapePx
    )

    drawArc(
        brush = Brush.radialGradient(
            colors = colors,
            center = Offset(
                x = size.width - innerShapePx - borderPx,
                y = innerShapePx + borderPx
            ),
            radius = innerShapePx
        ),
        size = sizeArc,
        topLeft = Offset(
            x = size.width - arcSize - borderPx,
            y = borderPx
        ),
        startAngle = -90f,
        sweepAngle = 90f,
        useCenter = true,
    )

    drawLine(
        brush = Brush.horizontalGradient(
            colors = colors,
            startX = innerShapePx + borderPx,
            endX = borderPx
        ),
        start = Offset(
            x = radiusShape + borderPx,
            y = innerShapePx + borderPx
        ),
        end = Offset(
            x = borderPx + radiusShape,
            y = size.height - innerShapePx - borderPx
        ),
        strokeWidth = innerShapePx
    )

    drawLine(
        brush = Brush.horizontalGradient(
            colors = colors,
            startX = size.width - innerShapePx - borderPx,
            endX = size.width - borderPx
        ),
        start = Offset(
            x = size.width - borderPx - radiusShape,
            y = innerShapePx + borderPx
        ),
        end = Offset(
            x = size.width - borderPx - radiusShape,
            y = size.height - innerShapePx - borderPx
        ),
        strokeWidth = innerShapePx
    )

    drawArc(
        brush = Brush.radialGradient(
            colors, center = Offset(
                x = innerShapePx + borderPx,
                y = size.height - innerShapePx - borderPx
            ), radius = innerShapePx
        ),
        size = sizeArc,
        topLeft = Offset(
            x = borderPx,
            y = size.height - arcSize - borderPx
        ),
        startAngle = 90f,
        sweepAngle = 90f,
        useCenter = true,
    )
    drawLine(
        brush = Brush.verticalGradient(
            colors = colors,
            startY = size.height - innerShapePx - borderPx,
            endY = size.height - borderPx,
        ),
        start = Offset(
            x = innerShapePx + borderPx,
            y = size.height - radiusShape - borderPx
        ),
        end = Offset(
            x = size.width - innerShapePx - borderPx,
            y = size.height - radiusShape - borderPx
        ),
        strokeWidth = innerShapePx
    )
    drawArc(
        brush = Brush.radialGradient(
            colors = colors,
            center = Offset(
                x = size.width - innerShapePx - borderPx,
                y = size.height - innerShapePx - borderPx
            ),
            radius = innerShapePx
        ),
        size = sizeArc,
        topLeft = Offset(
            x = size.width - arcSize - borderPx,
            y = size.height - arcSize - borderPx
        ),
        startAngle = 0f,
        sweepAngle = 90f,
        useCenter = true,
    )
}


@Preview
@Composable
private fun BoxPatternDotBG() {
    BoxPatternDot(Modifier.patternDotBG(space = 10.dp))
}

@Preview
@Composable
private fun BoxShapePatternDotBG() {
    BoxPatternDot(Modifier.patternDotBG(space = 10.dp, shapeSize = 125.dp))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBG() {
    BoxPatternDot(
        modifier = Modifier
            .patternDotBG(space = 10.dp)
            .border(
                width = 1.dp, color = Color.White.copy(0.25f), shape = RoundedCornerShape(32.dp)
            )
    )
}

@Preview
@Composable
private fun BoxPatternDotBrushBG() {
    BoxPatternDot(Modifier.patternDotBrushBG(space = 10.dp))
}

@Preview
@Composable
private fun BoxShapePatternDotBrushBG() {
    BoxPatternDot(Modifier.patternDotBrushBG(space = 10.dp, shapeSize = 125.dp))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBrushBG() {
    BoxPatternDot(
        modifier = Modifier
            .patternDotBrushBG(
                space = 10.dp,
                shadowColors = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    Color.White.copy(0.125f)
                )
            )
            .border(
                width = 1.dp, color = Color.White.copy(0.25f), shape = RoundedCornerShape(32.dp)
            )
    )
}

@Composable
private fun BoxPatternDot(modifier: Modifier) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(250.dp)
            .then(modifier)
    )
}*/
