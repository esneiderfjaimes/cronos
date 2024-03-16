@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)

package com.nei.cronos.feature.editchronometerformat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.component.ChronometerChip
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.utils.getLocale
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.ui.pages.format.EditFormat
import com.nei.cronos.utils.Mocks
import com.nei.cronos.utils.differenceParse
import cronos.core.model.ChronometerFormat
import java.time.ZonedDateTime

@Composable
fun EditFormatDialog(
    chronometer: ChronometerUi,
    format: ChronometerFormat = chronometer.format,
    onConfirmationDiscardChanges: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    startTimeProvider: () -> ZonedDateTime = { ZonedDateTime.now() },
    endTimeProvider: () -> ZonedDateTime = { ZonedDateTime.now() },
    onConfirmation: () -> Unit = {},
    onUpdate: (ChronometerFormat) -> Unit = {},
) {
    val hasChanges by remember(format, chronometer.format) {
        derivedStateOf { format.toFlags() != chronometer.format.toFlags() }
    }

    var showDialog by rememberSaveable { mutableStateOf(hasChanges) }

    val onBack = {
        if (hasChanges) {
            showDialog = true
        } else {
            onDismissRequest.invoke()
        }
    }

    if (showDialog) {
        AlertDiscardChanges(
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                showDialog = false
                onDismissRequest.invoke()
                onConfirmationDiscardChanges.invoke()
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

        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        CardContent(
            hasChanges = hasChanges,
            onDismissRequest = onBack,
            onConfirmation = onConfirmation,
            formatProvider = { format },
            onUpdate = onUpdate,
            startTimeProvider = startTimeProvider,
            endTimeProvider = endTimeProvider
        )
    }

    BackHandler(onBack = onBack)
}

@Composable
fun AlertDiscardChanges(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    AlertDialog(
        icon = {
            Icon(Icons.Rounded.Warning, contentDescription = "Example Icon")
        },
        title = {
            Text(text = "Discard your changes?")
        },
        text = {
            Text(text = "You have unsaved changes. Are you sure you want to discard them?")
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirmation,
                colors = ButtonDefaults.filledTonalButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text("Discard")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ColumnScope.CardContent(
    hasChanges: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    formatProvider: () -> ChronometerFormat,
    onUpdate: (ChronometerFormat) -> Unit,
    startTimeProvider: () -> ZonedDateTime = { ZonedDateTime.now() },
    endTimeProvider: () -> ZonedDateTime = { ZonedDateTime.now() },
) {
    val locale = getLocale()

    Surface(
        shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
        modifier = Modifier
            .sizeIn(maxWidth = 600.dp)
            .align(Alignment.CenterHorizontally)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = "format",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = onConfirmation,
                        enabled = hasChanges,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(text = "Save")
                    }
                }
                ChronometerChip(
                    text = differenceParse(
                        format = formatProvider.invoke(),
                        locale = locale,
                        startInclusive = startTimeProvider.invoke(),
                        endExclusive = endTimeProvider.invoke()
                    )
                )
            }

            item {
                EditFormat(
                    formatProvider = formatProvider,
                    onUpdate = onUpdate,
                    modifier = Modifier
                        .padding(16.dp),
                )
            }
            item {
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
}

@ThemePreviews
@Composable
fun EditChronometerPreview() {
    val chronometerLast = Mocks.chronometerPreview
    val lastFormat = chronometerLast.format
    val chronometer by remember { mutableStateOf(chronometerLast) }
    var format by remember { mutableStateOf(lastFormat) }
    val hasChanges by remember(format, lastFormat) {
        derivedStateOf { format.toFlags() != lastFormat.toFlags() }
    }

    CronosTheme {
        Column(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            CardContent(
                hasChanges = hasChanges,
                onConfirmation = {},
                formatProvider = { format },
                onUpdate = { format = it },
            )
        }
    }
}

@Composable
@ThemePreviews
private fun AlertDiscardChangesPreview() {
    CronosTheme {
        AlertDiscardChanges()
    }
}