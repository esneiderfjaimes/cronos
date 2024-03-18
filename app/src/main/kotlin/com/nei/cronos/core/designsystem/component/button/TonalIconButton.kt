package com.nei.cronos.core.designsystem.component.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

@Composable
fun TonalIconButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp)
    ) {
        Surface(
            onClick = onClick,
            modifier = modifier
                .semantics { role = Role.Button }
                .padding(bottom = 8.dp),
            shape = shape,
            // color = MaterialTheme.colorScheme.secondaryContainer,
            tonalElevation = 1.dp,
            interactionSource = interactionSource
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp), contentAlignment = Alignment.Center
            ) {
                icon.invoke()

            }
        }
        Text(label, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@ThemePreviews
@Composable
fun TonalIconButtonPreview() {
    CronosTheme {
        CronosBackground {
            TonalIconButton(
                onClick = {},
                label = "Add",
                icon = {
                    Icon(imageVector = Icons.Rounded.Favorite, contentDescription = null)
                },
            )
        }
    }
}