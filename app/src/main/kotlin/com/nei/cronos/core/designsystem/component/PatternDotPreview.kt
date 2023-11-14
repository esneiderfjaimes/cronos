package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val ColorBackground = Color(0xFF020518)
val ColorDot = Color(0xFF2B2D3C)
val BrushDot = Brush.linearGradient(
    listOf(Color.Blue, Color.Red),
)

private fun Modifier.previewPatternDot() = basePatternDot(
    colorBackground = ColorBackground,
    colorDot = ColorDot
)

private fun Modifier.previewPatternDotBrush() = basePatternDot(
    colorBackground = ColorBackground,
    brushDot = BrushDot,
)

// region Preview
/*
@Preview
@Composable
private fun BoxPatternDot() {
    BoxPatternDot(
        Modifier.previewPatternDot()
    )
}

@Preview
@Composable
private fun BoxShapePatternDot() {
    BoxPatternDot(Modifier.basePatternDot(shapeSize = 128.dp))
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
    BoxPatternDot(Modifier.basePatternDot())
}

@Preview
@Composable
private fun BoxShapePatternDotBrush() {
    BoxPatternDot(Modifier.basePatternDot(shapeSize = 125.dp))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBrush() {
    BoxPatternDot(
        modifier = Modifier
            .basePatternDot(
                shapeSize = (32.dp)
            )
            .border(
                width = 1.dp, color = Color.White, shape = RoundedCornerShape(32.dp)
            )
    )
}

@Preview
@Composable
private fun BoxPatternDotBG() {
    BoxPatternDot(Modifier.basePatternDot(space = 10.dp))
}

@Preview
@Composable
private fun BoxShapePatternDotBG() {
    BoxPatternDot(Modifier.basePatternDot(space = 10.dp, shapeSize = 125.dp))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBG() {
    BoxPatternDot(
        modifier = Modifier
            .basePatternDot(space = 10.dp)
            .border(
                width = 1.dp, color = Color.White.copy(0.25f), shape = RoundedCornerShape(32.dp)
            )
    )
}

@Preview
@Composable
private fun BoxPatternDotBrushBG() {
    BoxPatternDot(Modifier.basePatternDot(space = 10.dp))
}

@Preview
@Composable
private fun BoxShapePatternDotBrushBG() {
    BoxPatternDot(Modifier.basePatternDot(space = 10.dp, shapeSize = 125.dp))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBrushBG() {
    BoxPatternDot(
        modifier = Modifier
            .basePatternDot(
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

@Preview
@Composable
private fun BoxPatternDotBGBorder() {
    BoxPatternDot(Modifier.basePatternDot(space = 10.dp, border = 1.dp))
}

@Preview
@Composable
private fun BoxShapePatternDotBGBorder() {
    BoxPatternDot(Modifier.basePatternDot(space = 10.dp, shapeSize = 125.dp, border = 1.dp))
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBGBorder() {
    BoxPatternDot(
        modifier = Modifier
            .basePatternDot(space = 10.dp, border = 1.dp)
            .border(
                width = 1.dp, color = Color.White.copy(0.25f), shape = RoundedCornerShape(32.dp)
            )
    )
}

@Preview
@Composable
private fun BoxPatternDotBrushBGBorder() {
    BoxPatternDot(Modifier.basePatternDot(space = 10.dp, border = 10.dp))
}

@Preview
@Composable
private fun BoxShapePatternDotBrushBGBorder() {
    BoxPatternDot(
        Modifier.basePatternDot(
            space = 10.dp,
            shapeSize = 125.dp,
            border = 10.dp
        )
    )
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBrushBGBorder() {
    BoxPatternDot(
        modifier = Modifier
            .basePatternDot(
                space = 10.dp,
                shadowColors = listOf(
                    Color.Transparent,
                    Color.White.copy(0.125f)
                ),
                border = 10.dp
            )
    )
}

@Preview
@Composable
private fun BoxShapeBorderPatternDotBrushBGBorder2() {
    BoxPatternDot(
        modifier = Modifier
            .basePatternDot(
                space = 10.dp,
                shadowColors = listOf(
                    Color.Transparent,
                    Color.White.copy(0.125f)
                ),
                shapeSize = 125.dp,
                border = 10.dp,
                borderColor = Color.White
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
*/
// endregion