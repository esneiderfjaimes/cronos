@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDatePickerState(): DatePickerState {
    return rememberDatePickerState(
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