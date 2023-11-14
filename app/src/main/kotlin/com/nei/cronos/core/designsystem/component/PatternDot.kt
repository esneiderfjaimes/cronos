package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.patternDot(
    colorBackground: Color = Color(0xFF020518),
    shape: Shape = RectangleShape,
    colorDot: Color = Color(0xFF2B2D3C),
    dotSize: Dp = 2.dp,
    space: Dp = 25.dp
) = clip(shape)
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
    }

fun Modifier.patternDotBrush(
    colorBackground: Color = Color(0xFF020518),
    shape: Shape = RectangleShape,
    brushDot: Brush = Brush.linearGradient(
        listOf(Color.Blue, Color.Red),
    ),
    dotSize: Dp = 2.dp,
    space: Dp = 25.dp
) = clip(shape)
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
    }

internal fun getPointMode(
    width: Float,
    height: Float,
    spacePx: Float
): List<Offset> {
    val initWith = (width / 2) % spacePx
    val initHeight = (height / 2) % spacePx

    var x = initWith
    var y = initHeight

    val list = mutableListOf<Offset>()
    while (y <= height) {
        while (x <= width) {
            list.add(Offset(x, y))
            x += spacePx
        }
        x = initWith
        y += spacePx
    }

    return list
}

@Preview
@Composable
private fun BoxPatternDot() {
    BoxPatternDot(Modifier.patternDot())
}

@Preview
@Composable
private fun BoxShapePatternDot() {
    BoxPatternDot(Modifier.patternDot(shape = CircleShape))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDot() {
    BoxPatternDot(
        modifier = Modifier
            .patternDot(
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 1.dp, color = Color.White, shape = RoundedCornerShape(32.dp)
            )
    )
}

@Preview
@Composable
private fun BoxPatternDotBrush() {
    BoxPatternDot(Modifier.patternDotBrush())
}

@Preview
@Composable
private fun BoxShapePatternDotBrush() {
    BoxPatternDot(Modifier.patternDotBrush(shape = CircleShape))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBrush() {
    BoxPatternDot(
        modifier = Modifier
            .patternDotBrush(
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 1.dp, color = Color.White, shape = RoundedCornerShape(32.dp)
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
}
