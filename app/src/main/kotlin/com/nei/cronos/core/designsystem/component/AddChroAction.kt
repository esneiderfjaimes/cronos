package com.nei.cronos.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

@Composable
fun Action(
    isChecked: Boolean,
    text: @Composable () -> Unit,
    imageVector: ImageVector,
    onClick: () -> Unit,
    onCloseClick: () -> Unit = {},
    contentDescription: String = ""
) {
    val animatedColor by animateColorAsState(
        if (isChecked) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
        label = "color_action"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CircleShape)
            .drawBehind { drawRect(animatedColor) }
            .toggleable(
                value = isChecked,
                role = Role.Checkbox,
                onValueChange = { onClick.invoke() }
            )
            .semantics(mergeDescendants = true) {
                role = Role.Checkbox
                stateDescription = if (isChecked) "Checked" else "Unchecked"
                this.contentDescription = contentDescription
            },
    ) {
        AnimatedVisibility(visible = !isChecked) {
            Box(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector, contentDescription = null)
            }
        }
        AnimatedVisibility(visible = isChecked) {
            Row {
                Spacer(modifier = Modifier.width(12.dp))
                text.invoke()
            }
        }
        AnimatedVisibility(visible = isChecked) {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Rounded.Close, contentDescription = null)
            }
        }
    }
}

@ThemePreviews
@Composable
fun AddChronometerActionPreview() {
    CronosTheme {
        CronosBackground {
            Action(
                isChecked = false,
                text = { Text(text = "00:00") },
                imageVector = Icons.Rounded.Schedule,
                onClick = {}
            )
        }
    }
}

@ThemePreviews
@Composable
fun AddChronometerActionPreview3() {
    var isChecked by remember { mutableStateOf(true) }
    CronosTheme {
        CronosBackground {
            Action(
                isChecked = isChecked,
                text = { Text(text = "00:00") },
                imageVector = Icons.Rounded.Schedule,
                onClick = { isChecked = !isChecked },
                onCloseClick = { isChecked = false }
            )
        }
    }
}