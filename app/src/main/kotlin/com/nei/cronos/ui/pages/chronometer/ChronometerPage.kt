@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.nei.cronos.ui.pages.chronometer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.NeiLoading
import com.nei.cronos.core.designsystem.component.button.TonalIconButton
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.utils.getLocale
import com.nei.cronos.feature.editchronometer.EditChronometerDialog
import com.nei.cronos.feature.editchronometerformat.EditFormatDialog
import com.nei.cronos.ui.pages.chronometer.ChronometerViewModel.Event
import com.nei.cronos.ui.pages.chronometer.ChronometerViewModel.UiState
import com.nei.cronos.utils.FlowAsState
import com.nei.cronos.utils.Mocks
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType

@Composable
fun ChronometerRoute(
    onBackClick: () -> Unit,
    viewModel: ChronometerViewModel = hiltViewModel(),
) {
    val state: UiState by viewModel.state.collectAsStateWithLifecycle()
    var showEditAppearance by rememberSaveable { mutableStateOf(false) }
    var showEditFormat by rememberSaveable { mutableStateOf(false) }

    FlowAsState(flow = viewModel.events) { event ->
        when (event) {
            is Event.FinishUpdate -> {
                showEditFormat = false
            }
        }
    }

    ChronometerScreen(
        onBackClick = onBackClick,
        showEditAppearance = showEditAppearance,
        onEditChronometerChange = { showEditAppearance = it },
        showEditFormat = showEditFormat,
        onEditFormatChange = { showEditFormat = it },
        onConfirmationDiscardChanges = viewModel::onConfirmationDiscardChanges,
        onStopClick = { viewModel.addEvent(EventType.STOP) },
        onRestartClick = { viewModel.addEvent(EventType.RESTART) },
        onNewLapClick = { viewModel.addEvent(EventType.LAP) },
        onConfirmation = viewModel::updateFormat,
        onUpdate = viewModel::onFormatChange,
        onDeleteClick = {
            viewModel.deleteChronometer()
            onBackClick.invoke()
        },
        state = state
    )
}

@Composable
private fun ChronometerScreen(
    onBackClick: () -> Unit = {},
    showEditAppearance: Boolean = false,
    onEditChronometerChange: (Boolean) -> Unit = {},
    showEditFormat: Boolean = false,
    onEditFormatChange: (Boolean) -> Unit = {},
    onConfirmationDiscardChanges: () -> Unit = {},
    onStopClick: () -> Unit = {},
    onRestartClick: () -> Unit = {},
    onNewLapClick: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    onUpdate: (ChronometerFormat) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    state: UiState,
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Chronometer") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back Navigation"
                    )
                }
            }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(ScrollState(0)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state) {
                UiState.Error -> Text(text = "Error")
                UiState.Loading -> NeiLoading()
                is UiState.Success -> {
                    Body(
                        onEditChronometerClick = { onEditChronometerChange.invoke(true) },
                        onEditFormatClick = { onEditFormatChange.invoke(true) },
                        onDeleteClick = { onDeleteClick.invoke() },
                        onStopClick = onStopClick,
                        onRestartClick = onRestartClick,
                        onNewLapClick = onNewLapClick,
                        state = state
                    )
                }
            }
        }
    }

    if (state is UiState.Success) {
        if (showEditAppearance) {
            EditChronometerDialog(
                chronometerId = state.chronometer.id,
                title = state.chronometer.title,
                onDismissRequest = { onEditChronometerChange.invoke(false) }
            )
        }
        if (showEditFormat) {
            EditFormatDialog(
                chronometer = state.chronometer,
                format = state.tempFormat,
                onConfirmationDiscardChanges = onConfirmationDiscardChanges,
                onDismissRequest = { onEditFormatChange.invoke(false) },
                startTimeProvider = { state.timeRanges.first },
                endTimeProvider = { state.timeRanges.second },
                onConfirmation = onConfirmation,
                onUpdate = onUpdate
            )
        }
    }
}

@Composable
private fun Body(
    onEditChronometerClick: () -> Unit,
    onEditFormatClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStopClick: () -> Unit,
    onRestartClick: () -> Unit,
    onNewLapClick: () -> Unit,
    state: UiState.Success
) {
    val locale = getLocale()

    /*    ChronometerChip(
            text = differenceParse(
                format = state.chronometer.format,
                locale = locale,
                startInclusive = state.timeRanges.first,
                endExclusive = state.timeRanges.second
            ),
            modifier = Modifier.padding(32.dp),
        )*/

    Text(text = state.chronometer.title)

    Cosmetics(
        isPaused = state.chronometer.isPaused,
        onEditChronometerClick = onEditChronometerClick,
        onEditFormatClick = onEditFormatClick,
        onDeleteClick = { onDeleteClick.invoke() },
        onStopClick = onStopClick,
        onRestartClick = onRestartClick,
        onNewLapClick = onNewLapClick
    )
}


@Composable
fun Cosmetics(
    isPaused: Boolean,
    onEditChronometerClick: () -> Unit,
    onEditFormatClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStopClick: () -> Unit,
    onRestartClick: () -> Unit,
    onNewLapClick: () -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TonalIconButton(
            onClick = onEditChronometerClick,
            label = "Edit",
        ) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
        }
        TonalIconButton(
            onClick = onEditFormatClick,
            label = "Format",
        ) {
            Icon(imageVector = Icons.Rounded.Circle, contentDescription = null)
        }
/*        TonalIconButton(
            onClick = { onDeleteClick.invoke() },
            label = "Delete",
        ) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
        }*/

        Text(
            text = "Actions",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(4.dp),
            textAlign = TextAlign.Center
        )

/*        if (!isPaused) {
            TonalIconButton(
                onClick = { onStopClick.invoke() },
                label = "Stop",
            ) {
                Icon(imageVector = Icons.Rounded.Stop, contentDescription = null)
            }
        } else {
            TonalIconButton(
                onClick = { onRestartClick.invoke() },
                label = "Restart",
            ) {
                Icon(imageVector = Icons.Rounded.RestartAlt, contentDescription = null)
            }
        }
        TonalIconButton(
            onClick = { onNewLapClick.invoke() },
            label = "New Lap",
        ) {
            Icon(imageVector = Icons.Rounded.Flag, contentDescription = null)
        }*/
    }
}

@ThemePreviews
@Composable
private fun ChronometerPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            ChronometerScreen(
                state = UiState.Success(
                    Mocks.chronometerPreview
                ),
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ChronometerPreviewStop() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            ChronometerScreen(
                state = UiState.Success(
                    Mocks.chronometerPreview.copy(isActive = false)
                ),
                showEditFormat = true
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ChronometerLoadingPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            ChronometerScreen(
                state = UiState.Loading,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ChronometerErrorPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            ChronometerScreen(
                state = UiState.Error,
            )
        }
    }
}