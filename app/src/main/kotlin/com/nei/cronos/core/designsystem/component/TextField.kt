package com.nei.cronos.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun textFieldColorsTransparent() = TextFieldDefaults.colors(
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        .copy(alpha = 0.75f),
    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface
        .copy(alpha = 0.38f),
    errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
)
