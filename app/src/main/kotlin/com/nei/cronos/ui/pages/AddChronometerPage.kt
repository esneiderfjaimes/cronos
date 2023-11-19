@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nei.cronos.core.designsystem.component.AddChronometerAction
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.DatePickerDialog
import com.nei.cronos.core.designsystem.component.TextField
import com.nei.cronos.core.designsystem.component.TimePickerDialog
import com.nei.cronos.core.designsystem.component.rememberDatePickerState
import com.nei.cronos.core.designsystem.component.rememberTimePickerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChronometerPage(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onOpenBottomSheetChange: (Boolean) -> Unit = {},
) {
    val viewModel: AddChronometerViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    // focus requester
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        delay(250)
        focusRequester.requestFocus()
    }

    var title by rememberSaveable { mutableStateOf("") }
    val enabledButton by remember { derivedStateOf { title.isNotBlank() } }

    // date picker
    val dateState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // time picker
    val timeState = rememberTimePickerState()
    var selectedTime by rememberSaveable { mutableStateOf<Time?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }

    TextField(
        value = title,
        onValueChange = { title = it },
        placeholder = { Text("Name of the new stopwatch") },
        modifier = Modifier.focusRequester(focusRequester),
        keyboardActions = KeyboardActions {
            scope.launch {
                focusManager.clearFocus(true)
                delay(250)
                sheetState.hide()
                onOpenBottomSheetChange(false)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )
    Row(
        Modifier
            .navigationBarsPadding()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AddChronometerAction(
            formattedValue = dateState.selectedDateMillis?.let {

                // Obtén el Locale del dispositivo
                val locale = Locale.getDefault()

                // Obtiene el patrón de formato de fecha del Locale del dispositivo
                val dateFormatPattern = DateTimeFormatter.ofPattern("d/M/yyyy", locale)

                // Obtiene la fecha actual (sin hora)
                val currentDate =  Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()

                // Formatea la fecha utilizando el formatter
                val formattedDate = currentDate.format(dateFormatPattern)

                formattedDate
            },
            onClick = { showDatePicker = true },
            onCloseClick = { dateState.selectedDateMillis = null }
        ) {
            Icon(Icons.Outlined.CalendarToday, contentDescription = null)
        }
        AddChronometerAction(
            formattedValue = selectedTime?.let { "${it.first}:${it.second}" },
            onClick = { showTimePicker = true },
            onCloseClick = { selectedTime = null }
        ) {
            Icon(Icons.Outlined.Schedule, contentDescription = null)
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = {
                viewModel.insertChronometer(
                    title = title,
                    selectedTime = selectedTime,
                    selectedDateMillis = dateState.selectedDateMillis,
                )
                scope.launch {
                    focusManager.clearFocus(true)
                    delay(250)
                    sheetState.hide()
                    onOpenBottomSheetChange(false)
                }
            },
            enabled = enabledButton
        ) {
            Text(text = "Start")
        }
    }

    TimePickerDialog(
        showTimePicker = showTimePicker,
        state = timeState,
        onCancel = { showTimePicker = false },
        onConfirm = {
            selectedTime = Triple(timeState.hour, timeState.minute, timeState.is24hour)
            showTimePicker = false
        }
    )

    DatePickerDialog(
        showDatePicker = showDatePicker,
        state = dateState,
        onConfirm = { showDatePicker = false },
        onCancel = { showDatePicker = false }
    )
}


@Composable
fun InputChipExample(
    text: String,
    onDismiss: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        label = { Text(text) },
        selected = enabled,
        avatar = {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Localized description",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Localized description",
                    Modifier.size(InputChipDefaults.AvatarSize)
                )
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Composable
fun AddChronometerPagePreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            Column {
                AddChronometerPage()
            }
        }
    }
}

