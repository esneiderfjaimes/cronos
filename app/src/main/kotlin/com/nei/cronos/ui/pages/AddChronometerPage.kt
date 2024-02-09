@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.nei.cronos.core.designsystem.component.TimePickerPlusState
import com.nei.cronos.core.designsystem.component.rememberDatePickerState
import com.nei.cronos.core.designsystem.component.rememberTimePickerPlusState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnrememberedMutableState")
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
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    // time picker
    val timeState = rememberTimePickerPlusState()

    AddChronometerContent(
        // title
        title = title,
        onTitleChange = { title = it },
        focusRequester = focusRequester,
        // date
        dateState = dateState,
        showDatePicker = showDatePicker,
        onShowDatePickerChange = { showDatePicker = it },
        // time
        timeState = timeState,
        // buttons
        addChronometerClick = {
            scope.launch {
                viewModel.insertChronometer(
                    title = title,
                    selectedTime = timeState.time,
                    selectedDateMillis = dateState.selectedDateMillis,
                )
                focusManager.clearFocus(true)
                delay(250)
                sheetState.hide()
                onOpenBottomSheetChange(false)
            }
        },
        enabledButton = enabledButton
    )
}

@Composable
private fun AddChronometerContent(
    // title
    title: String,
    onTitleChange: (String) -> Unit,
    focusRequester: FocusRequester,
    // date
    dateState: DatePickerState,
    showDatePicker: Boolean,
    onShowDatePickerChange: (Boolean) -> Unit,
    // title
    timeState: TimePickerPlusState,
    // buttons
    addChronometerClick: () -> Unit,
    enabledButton: Boolean
) {
    TextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = { Text("Name of the new stopwatch") },
        modifier = Modifier.focusRequester(focusRequester),
        keyboardActions = KeyboardActions {
            addChronometerClick.invoke()
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
                val currentDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()

                // Formatea la fecha utilizando el formatter
                val formattedDate = currentDate.format(dateFormatPattern)

                formattedDate
            },
            onClick = { onShowDatePickerChange.invoke(true) },
            onCloseClick = { dateState.selectedDateMillis = null }
        ) {
            Icon(Icons.Outlined.CalendarToday, contentDescription = null)
        }
        AddChronometerAction(
            formattedValue = timeState.time?.let { "${it.hour}:${it.minute}" },
            onClick = { timeState.showingDialog = true },
            onCloseClick = { timeState.time = null }
        ) {
            Icon(Icons.Outlined.Schedule, contentDescription = null)
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = addChronometerClick,
            enabled = enabledButton
        ) {
            Text(text = "Start")
        }
    }

    TimePickerDialog(
        state = timeState
    )

    DatePickerDialog(
        showDatePicker = showDatePicker,
        state = dateState,
        onConfirm = { onShowDatePickerChange.invoke(false) },
        onCancel = { onShowDatePickerChange.invoke(false) }
    )
}

@ThemePreviews
@Composable
fun AddChronometerPagePreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            Column {
                AddChronometerContent(
                    title = "",
                    onTitleChange = {},
                    focusRequester = FocusRequester(),
                    dateState = rememberDatePickerState(
                        Instant.now().toEpochMilli()
                    ),
                    showDatePicker = false,
                    onShowDatePickerChange = {},
                    timeState = rememberTimePickerPlusState(),
                    addChronometerClick = {},
                    enabledButton = true
                )
            }
        }
    }
}
