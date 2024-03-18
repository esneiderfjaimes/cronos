package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

@Composable
fun SwitchFormat(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    val colorContainer = if (isSystemInDarkTheme()) Color.Black else Color.White
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .semantics(mergeDescendants = true) {
                this.contentDescription = "Switch $text"
                this.role = Role.Switch
            }
            .minimumInteractiveComponentSize(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        border = if (checked) BorderStroke(2.dp, color) else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange.invoke(!checked) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, modifier = Modifier.weight(1f))
            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange.invoke(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorContainer,
                    checkedTrackColor = color,
                    checkedBorderColor = color,
                    checkedIconColor = Color.Cyan,
                    uncheckedThumbColor = color,
                    uncheckedTrackColor = colorContainer,
                    uncheckedBorderColor = color,
                    uncheckedIconColor = Color.Red,
                ),
            )
        }
    }
}

@ThemePreviews
@Composable
fun EditFormatPreview() {
    CronosTheme {
        CronosBackground {
            Column {
                SwitchFormat("Title, example checked", true) {}
                SwitchFormat("Title, example unchecked", false) {}
            }
        }
    }
}