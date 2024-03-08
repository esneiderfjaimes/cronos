@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

package com.nei.cronos.ui.pages.addchronometer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.nei.cronos.core.designsystem.component.Action
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
import java.util.Locale

@Composable
fun AddChronometerPage(
    openBottomSheet: Boolean,
    onOpenBottomSheetChange: (Boolean) -> Unit = {}
) {
    if (openBottomSheet) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.scrim.copy(0.32f))
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput({
                    onOpenBottomSheetChange.invoke(false)
                }) {
                    detectTapGestures {
                        onOpenBottomSheetChange.invoke(false)
                    }
                }
                .clearAndSetSemantics {}
            )

            Surface(
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .sizeIn(maxWidth = 600.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Column {
                    AddChronometerPage(
                        onOpenBottomSheetChange = onOpenBottomSheetChange
                    )
                    Spacer(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxWidth()
                            .height(
                                (if (WindowInsets.isImeVisible) WindowInsets.ime
                                else WindowInsets.navigationBars)
                                    .asPaddingValues()
                                    .calculateBottomPadding()
                            )
                    )
                }
            }
        }

        BackHandler {
            onOpenBottomSheetChange.invoke(false)
        }
    }
}

@Composable
fun AddChronometerPage(
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
        addChronometerClick = click@{
            if (title.isBlank()) return@click
            scope.launch {
                viewModel.insertChronometer(
                    title = title,
                    selectedTime = timeState.time,
                    selectedDateMillis = dateState.selectedDateMillis,
                )
                focusManager.clearFocus(true)
                delay(250)
                onOpenBottomSheetChange(false)
            }
        },
        closeBottomSheet = { onOpenBottomSheetChange(false) },
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
    // time
    timeState: TimePickerPlusState,
    // buttons
    addChronometerClick: () -> Unit,
    enabledButton: Boolean,
    dateFormatter: DatePickerFormatter = remember {
        DatePickerDefaults.dateFormatter(
            selectedDateSkeleton = "MMddyy",
            yearSelectionSkeleton = "MMddyy",
        )
    },
    // bottom sheet
    closeBottomSheet: () -> Unit = {},
) {
    val showSpacerVertical by remember {
        derivedStateOf { dateState.selectedDateMillis != null || timeState.time != null }
    }
    val showSpacerHorizontal by remember {
        derivedStateOf { dateState.selectedDateMillis != null && timeState.time != null }
    }

    TextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = { Text("Name of the new stopwatch") },
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter) {
                    addChronometerClick()
                    return@onKeyEvent true
                }
                if (keyEvent.key == Key.Escape) {
                    closeBottomSheet()
                    return@onKeyEvent true
                }
                return@onKeyEvent false
            },
        keyboardActions = KeyboardActions(onDone = {
            addChronometerClick()
        }),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = if (enabledButton) ImeAction.Done else ImeAction.None
        ),
    )

    AnimatedVisibility(visible = showSpacerVertical) {
        Spacer(modifier = Modifier.height(16.dp))
    }
    FlowRow(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Action(
            isChecked = dateState.selectedDateMillis != null,
            text = {
                val defaultLocale = defaultLocale()
                val formattedDate = dateFormatter.formatDate(
                    dateMillis = dateState.selectedDateMillis,
                    locale = defaultLocale
                )
                Text(text = formattedDate ?: "")
            },
            imageVector = Icons.Rounded.CalendarToday,
            onClick = { onShowDatePickerChange.invoke(true) },
            onCloseClick = { dateState.selectedDateMillis = null },
            contentDescription = "Show date picker"
        )

        AnimatedVisibility(visible = showSpacerHorizontal) {
            Spacer(modifier = Modifier.width(4.dp))
        }

        Action(
            isChecked = timeState.time != null,
            text = { Text(text = timeState.time?.format() ?: "") },
            imageVector = Icons.Rounded.Schedule,
            onClick = { timeState.showingDialog = true },
            onCloseClick = { timeState.time = null },
            contentDescription = "Show time picker"
        )

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = addChronometerClick,
            enabled = enabledButton,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(text = "Start")
        }
    }

    AnimatedVisibility(visible = showSpacerVertical) {
        Spacer(modifier = Modifier.height(8.dp))
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

@Composable
@ReadOnlyComposable
internal fun defaultLocale(): CalendarLocale {
    return ConfigurationCompat.getLocales(LocalConfiguration.current).get(0) ?: Locale.getDefault()
}

@ThemePreviews
@Composable
fun AddChronometerPagePreview() {
    BasePreview()
}

@ThemePreviews
@Composable
fun AddChronometerPageSelectDatePreview() {
    BasePreview(
        dateState = rememberDatePickerState(
            initialSelectedDateMillis = Instant.now().toEpochMilli()
        )
    )
}

@ThemePreviews
@Composable
fun AddChronometerPageSelectTimePreview() {
    BasePreview(
        pickerPlusState = rememberTimePickerPlusState(initialHour = 20, initialMinute = 45)
    )
}

@ThemePreviews
@Composable
fun AddChronometerPageSelectDateAndTimePreview() {
    BasePreview(
        dateState = rememberDatePickerState(
            initialSelectedDateMillis = Instant.now().toEpochMilli()
        ),
        pickerPlusState = rememberTimePickerPlusState(initialHour = 20, initialMinute = 45)
    )
}

@Composable
private fun BasePreview(
    dateState: DatePickerState = rememberDatePickerState(),
    pickerPlusState: TimePickerPlusState = rememberTimePickerPlusState(),
) {
    CronosTheme {
        Column(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            // Spacer(modifier = Modifier.weight(1f))
            Surface(
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .sizeIn(maxWidth = 600.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Column {
                    AddChronometerContent(
                        title = "",
                        onTitleChange = {},
                        focusRequester = FocusRequester(),
                        dateState = dateState,
                        showDatePicker = false,
                        onShowDatePickerChange = {},
                        timeState = pickerPlusState,
                        addChronometerClick = {},
                        enabledButton = true
                    )
                }
            }
        }
    }
}