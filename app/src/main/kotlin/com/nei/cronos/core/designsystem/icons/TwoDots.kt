package com.nei.cronos.core.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

val Icons.Rounded.TwoDots: ImageVector
    get() {
        if (_twoDots != null) {
            return _twoDots!!
        }
        _twoDots = materialIcon(name = "Rounded.Circle") {
            materialPath {
                dot(12.0f, 2.0f, 8.66f, 2f)
                dot(12.0f, 15.33f, 22F, 15.33f)
                close()
            }
        }
        return _twoDots!!
    }

private fun PathBuilder.dot(x: Float, y: Float, y1: Float, y2: Float) {
    moveTo(x, y)
    arcTo(
        horizontalEllipseRadius = 1F,
        verticalEllipseRadius = 1F,
        theta = 0F,
        isMoreThanHalf = false,
        isPositiveArc = false,
        x1 = 12F,
        y1 = y1
    )
    arcTo(
        horizontalEllipseRadius = 1F,
        verticalEllipseRadius = 1F,
        theta = 0F,
        isMoreThanHalf = false,
        isPositiveArc = false,
        x1 = 12F,
        y1 = y2
    )
}

private var _twoDots: ImageVector? = null

@ThemePreviews
@Composable
private fun TwoDotsPreview() {
    CronosTheme {
        CronosBackground {
            Icon(Icons.Rounded.TwoDots, contentDescription = null)
        }
    }
}