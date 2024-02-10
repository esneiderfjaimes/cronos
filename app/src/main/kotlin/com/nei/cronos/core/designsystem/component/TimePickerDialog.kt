@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

@Composable
fun TimePickerDialog(
    state: TimePickerPlusState,
) {
    val configuration = LocalConfiguration.current

    val showingPicker by remember(state.displayMode) {
        derivedStateOf { state.displayMode == DisplayMode.Picker && configuration.screenHeightDp > 400 }
    }

    if (state.showingDialog) {
        TimePickerDialog(
            title = if (showingPicker) {
                "Select Time "
            } else {
                "Enter Time"
            },
            onCancel = {
                state.hideDialog()
            },
            onConfirm = {
                state.time = Time(
                    hour = state.timePickerState.hour,
                    minute = state.timePickerState.minute,
                    is24hour = state.timePickerState.is24hour
                )
                state.hideDialog()
            },
            toggle = {
                if (configuration.screenHeightDp > 400) {
                    IconButton(onClick = { state.toggleDisplayMode() }) {
                        val icon = if (state.displayMode == DisplayMode.Picker) {
                            Icons.Outlined.Keyboard
                        } else {
                            Icons.Outlined.Schedule
                        }
                        Icon(
                            icon,
                            contentDescription = if (showingPicker) {
                                "Switch to Text Input"
                            } else {
                                "Switch to Touch Input"
                            }
                        )
                    }
                }
            }
        ) {
            if (showingPicker) {
                TimePicker(state = state.timePickerState)
            } else {
                TimeInput(state = state.timePickerState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    title: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    BasicAlertDialog(onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                content.invoke()
                Row {
                    toggle.invoke()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = onConfirm,
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun TimePickerDialogPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            TimePickerDialog(
                state = rememberTimePickerPlusState(),
            )
        }
    }
}

@ThemePreviews
@Composable
fun TimePickerDialogInitialPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            TimePickerDialog(
                state = rememberTimePickerPlusState(initialDisplayMode = DisplayMode.Input),
            )
        }
    }
}