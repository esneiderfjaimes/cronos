@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import androidx.compose.material3.rememberDatePickerState as rememberDatePickerStateBase

@Composable
fun rememberDatePickerState(
    @Suppress("AutoBoxing") initialSelectedDateMillis: Long? = null,
    @Suppress("AutoBoxing") initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
): DatePickerState {
    return rememberDatePickerStateBase(
        initialSelectedDateMillis = initialSelectedDateMillis,
        initialDisplayedMonthMillis = initialDisplayedMonthMillis,
        initialDisplayMode = initialDisplayMode,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return System.currentTimeMillis() >= utcTimeMillis // TODO: check if it's in the future
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun DatePickerDialog(
    showDatePicker: Boolean,
    state: DatePickerState,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDatePicker) {
        val confirmEnabled by derivedStateOf { state.selectedDateMillis != null }
        DatePickerDialog(
            onDismissRequest = onCancel,
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@ThemePreviews
@Composable
fun DatePickerDialogPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            DatePickerDialog(
                showDatePicker = true,
                state = rememberDatePickerState(
                    initialDisplayMode = DisplayMode.Input
                ),
                onCancel = {},
                onConfirm = {}
            )
        }
    }
}

@ThemePreviews
@Composable
fun DatePickerDialogInitialPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            DatePickerDialog(
                showDatePicker = true,
                state = rememberDatePickerState(
                    initialSelectedDateMillis = System.currentTimeMillis()
                ),
                onCancel = {},
                onConfirm = {}
            )
        }
    }
}