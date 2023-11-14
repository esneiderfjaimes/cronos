package com.nei.cronos.core.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.textFieldColorsTransparent
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.theme.CronosTheme
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
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text("Name of the new stopwatch") },
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth()
            .focusRequester(focusRequester),
        colors = textFieldColorsTransparent(),
        keyboardActions = KeyboardActions {
            scope.launch {
                focusManager.clearFocus(true)
                delay(250)
                sheetState.hide()
                onOpenBottomSheetChange(false)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        )
    )
    Row(
        Modifier
            .navigationBarsPadding()
            .padding(horizontal = 6.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = {
            scope.launch {
                focusManager.clearFocus(true)
                delay(250)
                sheetState.hide()
                onOpenBottomSheetChange(false)
            }
        }, modifier = Modifier.padding(6.dp)) {
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

