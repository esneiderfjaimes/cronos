package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
fun ChronometerFlag(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ChronometerFlagIml(
            text = text,
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
        )
    }
}


@Composable
private fun ChronometerFlagIml(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    Surface(
        modifier = modifier
            .sizeIn(maxWidth = 175.dp, minWidth = 75.dp)
            .aspectRatio(1f, true)
            .padding(8.dp)
            .semantics(mergeDescendants = true) {
                this.contentDescription = "Switch $text background"
                this.role = Role.Checkbox
            },
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        border = if (checked) BorderStroke(2.dp, color) else null,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = text, maxLines = 1)
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    disabledCheckedColor = color,
                ),
                enabled = false
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange.invoke(!checked) }
        )
    }
}

@ThemePreviews
@Composable
fun ChronometerFlagPreview() {
    var checked by rememberSaveable { mutableStateOf(true) }
    CronosTheme {
        CronosBackground(Modifier.size(200.dp)) {
            ChronometerFlag(
                text = "Seconds",
                checked = checked,
            ) { checked = it }
        }
    }
}