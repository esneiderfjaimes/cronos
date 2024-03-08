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
    isLandscape: Boolean,
    translationXExtra: Float = 0f,
    panelOffsetX: Float,
    drawerWidth: Float,
    percentageTranslationShadow: Float = 0f,
    scaleStart: Float = 1f,
    scaleEnd: Float
) {
    val amount = offset / panelOffsetX // progress

    shadowTranslationEffect(
        amount = amount,
        isLandscape = isLandscape,
        translationXExtra = translationXExtra,
        offset = offset,
        percentageTranslationShadow = percentageTranslationShadow,
        drawerWidth = drawerWidth,
        panelOffsetX = panelOffsetX
    )

    scaleEffect(
        amount = amount,
        scaleStart = scaleStart,
        scaleEnd = scaleEnd
    )
}

@SuppressLint("RestrictedApi")
internal fun GraphicsLayerScope.shadowTranslationEffect(
    amount: Float,
    isLandscape: Boolean,
    translationXExtra: Float = 0f,
    offset: Float,
    percentageTranslationShadow: Float = 0f,
    drawerWidth: Float,
    panelOffsetX: Float
) {
    // Shadow translation effect
    translationX = if (isLandscape) {
        val translationXExtraPx = MathUtils.lerp(0f, translationXExtra, amount)
        offset + translationXExtraPx
    } else {
        val transStartX = 0f
        val transEndX = (panelOffsetX - drawerWidth) * percentageTranslationShadow
        val calTranslationX = MathUtils.lerp(transStartX, transEndX, amount)
        offset - calTranslationX
    }
}

@SuppressLint("RestrictedApi")
internal fun GraphicsLayerScope.scaleEffect(
    amount: Float,
    scaleStart: Float = 1f,
    scaleEnd: Float,
) {
    // Scale
    val scaleTransform = MathUtils.lerp(scaleStart, scaleEnd, amount)
    val sizeWidthScale = size.width * scaleTransform
    val borderWidth = size.width - sizeWidthScale
    translationX -= (borderWidth / 2)
    scaleX = scaleTransform
    scaleY = scaleTransform
}

@SuppressLint("RestrictedApi")
internal fun GraphicsLayerScope.shadowEffect(
    corners: Dp,
    shadowColor: Color
) {
    clip = corners > 0.dp
    spotShadowColor = shadowColor
    ambientShadowColor = shadowColor
    shadowElevation = corners.toPx()
    shape = RoundedCornerShape(corners)
}