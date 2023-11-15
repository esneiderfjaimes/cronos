package com.nei.cronos.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.TextField
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.AddChronometerPage(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onOpenBottomSheetChange: (Boolean) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(250)
        focusRequester.requestFocus()
    }
    var text by rememberSaveable { mutableStateOf("") }
    val enabledButton by remember { derivedStateOf { text.isNotBlank() } }
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = { text = it },
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
            .padding(horizontal = 4.dp)
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Outlined.Star, contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Outlined.Schedule, contentDescription = null)
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = {
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

