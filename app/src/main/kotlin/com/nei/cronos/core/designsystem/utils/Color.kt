package com.nei.cronos.core.designsystem.utils

import androidx.compose.ui.graphics.Color

operator fun Color.plus(other: Color) = blendColors(this, other)

fun blendColors(color1: Color, color2: Color): Color {
    val blendedRed = color1.red * (1 - color2.alpha) + color2.red * color2.alpha
    val blendedGreen = color1.green * (1 - color2.alpha) + color2.green * color2.alpha
    val blendedBlue = color1.blue * (1 - color2.alpha) + color2.blue * color2.alpha
    val blendedAlpha = color1.alpha * (1 - color2.alpha) + color2.alpha
    return Color(red = blendedRed, green = blendedGreen, blue = blendedBlue, alpha = blendedAlpha)
}
