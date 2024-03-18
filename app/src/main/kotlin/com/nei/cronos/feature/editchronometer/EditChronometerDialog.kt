@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)

package com.nei.cronos.feature.editchronometer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nei.cronos.core.designsystem.component.TextField
import com.nei.cronos.core.designsystem.dialog.BaseAlertDialog
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.utils.FlowAsState
import kotlinx.coroutines.delay

@Composable
fun EditChronometerDialog(
    chronometerId: Long = 0L,
    title: String,
    onDismissRequest: (Boolean) -> Unit = {},
    viewModel: EditChronometerViewModel = hiltViewModel(
        creationCallback = { factory: EditChronometerViewModel.Factory ->
            factory.create(chronometerId)
        }
    ),
) {
    FlowAsState(flow = viewModel.events) { event ->
        when (event) {
            is EditChronometerEvent.FinishUpdate -> {
                onDismissRequest.invoke(false)
            }
        }
    }

    var label by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = title,
                selection = TextRange(title.length, title.length)
            )
        )
    }

    val hasChanges by remember(title) {
        derivedStateOf { label.text.isNotBlank() && label.text != title }
    }

    var showDialog by rememberSaveable { mutableStateOf(hasChanges) }

    val onBack = {
        if (hasChanges) {
            showDialog = true
        } else {
            onDismissRequest.invoke(false)
        }
    }

    // focus requester
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(250)
        focusRequester.requestFocus()
    }

    if (showDialog) {
        BaseAlertDialog(
            title = "Discard your changes?",
            text = "You have unsaved changes. Are you sure you want to discard them?",
            confirmButtonText = "Discard",
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                onDismissRequest.invoke(false)
            }
        )
    }

    Column(
        Modifier
            .background(MaterialTheme.colorScheme.scrim.copy(0.32f))
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .pointerInput({
                onBack.invoke()
            }) {
                detectTapGestures {
                    onBack.invoke()
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
                EditChronometerContent(
                    focusRequester = focusRequester,
                    label = label,
                    onLabelChange = { label = it },
                    hasChanges = hasChanges,
                    onBackClick = onBack,
                    onSaveClick = { viewModel.update(it) }
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

    BackHandler(onBack = onBack)
}

@Composable
private fun EditChronometerContent(
    label: TextFieldValue = TextFieldValue("", TextRange.Zero),
    onLabelChange: (TextFieldValue) -> Unit = {},
    hasChanges: Boolean = false,
    onBackClick: () -> Unit = {},
    onSaveClick: (String) -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp),
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            text = "edit",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onSaveClick.invoke(label.text) },
            enabled = hasChanges,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text(text = "Save")
        }
    }

    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp, top = 16.dp),
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.small
    ) {
        TextField(
            label,
            onValueChange = onLabelChange,
            placeholder = { Text("enter a label") },
            modifier = Modifier
                .padding(8.dp)
                .focusRequester(focusRequester)
                .onKeyEvent { keyEvent ->
                    // Nothing to do
                    return@onKeyEvent keyEvent.key == Key.Enter
                },
            keyboardOptions = KeyboardOptions.Default,
        )
    }
}

@ThemePreviews
@Composable
fun EditChronometerPreview() {
    CronosTheme {
        Column(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .sizeIn(maxWidth = 600.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Column {
                    EditChronometerContent()
                }
            }
        }
    }
}