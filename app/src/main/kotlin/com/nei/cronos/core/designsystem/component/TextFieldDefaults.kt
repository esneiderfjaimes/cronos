package com.nei.cronos.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

@Immutable
object TextFieldDefaults {
    @Composable
    fun colors(
        focusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
        focusedPlaceholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        brush: Brush = SolidColor(MaterialTheme.colorScheme.primary)
    ): TextFieldColors {
        return TextFieldColors(
            focusedTextColor = focusedTextColor,
            focusedPlaceholderColor = focusedPlaceholderColor,
            brush = brush
        )
    }
}

/**
 * Represents the colors of the input text, container, and content (including label, placeholder,
 * leading and trailing icons) used in a text field in different states.
 *
 * @constructor create an instance with arbitrary colors.
 * See [TextFieldDefaults.colors] for the default colors used in [TextField].
 *
 * @param focusedTextColor the color used for the input text of this text field when focused
 * @param focusedPlaceholderColor the placeholder color for this text field when focused
 */
@Immutable
class TextFieldColors constructor(
    val focusedTextColor: Color,
    val focusedPlaceholderColor: Color,
    val brush: Brush
)