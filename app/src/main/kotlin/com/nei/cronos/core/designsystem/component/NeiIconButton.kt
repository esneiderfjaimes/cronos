package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

@Composable
fun NeiIconButton(
    iconVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    label: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        NeiIconButton(iconVector, contentDescription, onClick)
        Spacer(modifier = Modifier.height(4.dp))
        label?.invoke()
    }
}

@Composable
fun NeiIconButton(
    iconVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    Surface(
        modifier = Modifier
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescription ?: ""
                this.role = Role.Button
            },
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        border = BorderStroke(2.dp, color),
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(iconVector, contentDescription = null)
        }
    }
}

@ThemePreviews
@Composable
fun IconButtonPreview() {
    CronosTheme {
        CronosBackground {
            NeiIconButton(
                Icons.Rounded.Flag,
                modifier = Modifier.padding(8.dp),
                label = { Text("Flag") }
            ) {}
        }
    }
}