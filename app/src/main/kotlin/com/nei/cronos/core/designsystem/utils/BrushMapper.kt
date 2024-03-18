package com.nei.cronos.core.designsystem.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.component.ChronometerChip
import cronos.core.model.Brush
import cronos.core.model.LinearGradient
import cronos.core.model.LinearGradient.Direction
import cronos.core.model.RadialGradient
import cronos.core.model.SolidBackground
import androidx.compose.ui.graphics.Brush as BrushGraph

fun Brush.toBrush() = when (this) {
    is RadialGradient -> {
        BrushGraph.radialGradient(colorStops = colorStopsInsToColor(colorStops))
    }

    is LinearGradient -> {
        val (start, end) = direction.genOffsets()
        BrushGraph.linearGradient(
            colorStops = colorStopsInsToColor(colorStops),
            start = start,
            end = end
        )
    }

    is SolidBackground -> {
        SolidColor(Color(color))
    }
}

private fun colorStopsInsToColor(colorStops: Array<Pair<Float, Int>>) =
    colorStops.map { it.first to Color(it.second) }.toTypedArray()

private fun Direction.genOffsets() = when (this) {
    Direction.TOP -> Offset.InfiniteY to Offset.Zero
    Direction.RIGHT -> Offset.InfiniteX to Offset.Zero
    Direction.BOTTOM -> Offset.Zero to Offset.InfiniteY
    Direction.LEFT -> Offset.Zero to Offset.InfiniteX
    Direction.TOP_LEFT -> Offset.InfiniteY to Offset.InfiniteX
    Direction.TOP_RIGHT -> Offset.Infinite to Offset.Zero
    Direction.BOTTOM_LEFT -> Offset.Zero to Offset.Infinite
    Direction.BOTTOM_RIGHT -> Offset.InfiniteX to Offset.InfiniteY
}

val Offset.Companion.InfiniteX get() = Offset(Float.POSITIVE_INFINITY, 0f)

val Offset.Companion.InfiniteY get() = Offset(0f, Float.POSITIVE_INFINITY)

@Preview
@Composable
fun BrushPreview() {
    val backgrounds = listOf(
        "radial-gradient(0 to #EEAECA, 1 to #94BBE9);",
        "linear-gradient(direction:bottom, 0 to #EEAECA, 1 to #94BBE9);",
        "solid-color(#EEAECA);",
    )

    Column {
        backgrounds.map { Brush.parse(it).toBrush() }
            .forEach { brush ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(brush)
                )
                ChronometerChip(text = "00:00", brush = brush)
            }
    }
}