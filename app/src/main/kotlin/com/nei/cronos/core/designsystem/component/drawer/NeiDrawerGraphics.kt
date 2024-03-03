package com.nei.cronos.core.designsystem.component.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils

@SuppressLint("RestrictedApi")
internal fun GraphicsLayerScope.drawAnimation(
    offset: Float,
    scaleStart: Float = 1f,
    scaleEnd: Float,
    percentageTranslationShadow: Float = 0f,
    drawerWidth: Float,
    color: Color,
    corners: Dp = 32.dp,
    panelOffsetX: Float
) {
    val amount = offset / panelOffsetX // progress

    shadowTranslationEffect(
        amount = amount,
        offset = offset,
        percentageTranslationShadow = percentageTranslationShadow,
        drawerWidth = drawerWidth,
        panelOffsetX = panelOffsetX
    )

    scaleAndShape(
        amount = amount,
        scaleStart = scaleStart,
        scaleEnd = scaleEnd,
        color = color,
        corners = corners
    )
}

@SuppressLint("RestrictedApi")
internal fun GraphicsLayerScope.shadowTranslationEffect(
    amount: Float,
    offset: Float,
    percentageTranslationShadow: Float = 0f,
    drawerWidth: Float,
    panelOffsetX: Float
) {
    // Shadow translation effect
    val transStartX = 0f
    val transEndX = (panelOffsetX - drawerWidth) * percentageTranslationShadow
    val calTranslationX = MathUtils.lerp(transStartX, transEndX, amount)
    translationX = offset - calTranslationX
}

@SuppressLint("RestrictedApi")
internal fun GraphicsLayerScope.scaleAndShape(
    amount: Float,
    scaleStart: Float = 1f,
    scaleEnd: Float,
    color: Color,
    corners: Dp = 32.dp,
) {
    // Scale
    val scaleTransform = MathUtils.lerp(scaleStart, scaleEnd, amount)
    val sizeWidthScale = size.width * scaleTransform
    val borderWidth = size.width - sizeWidthScale
    translationX -= (borderWidth / 2)
    scaleX = scaleTransform
    scaleY = scaleTransform

    clip = true
    spotShadowColor = color
    shadowElevation = corners.toPx()
    shape = RoundedCornerShape(corners)
}