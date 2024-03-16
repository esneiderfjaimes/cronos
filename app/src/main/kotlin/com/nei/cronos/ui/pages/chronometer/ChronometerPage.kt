@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.nei.cronos.ui.pages.chronometer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.cronos.core.designsystem.component.ChronometerChip
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.NeiIconButton
import com.nei.cronos.core.designsystem.component.NeiLoading
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.utils.getLocale
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.feature.editchronometer.EditChronometerDialog
import com.nei.cronos.feature.editchronometerformat.EditFormatDialog
import com.nei.cronos.ui.pages.chronometer.ChronometerViewModel.Event
import com.nei.cronos.ui.pages.chronometer.ChronometerViewModel.UiState
import com.nei.cronos.utils.FlowAsState
import com.nei.cronos.utils.Mocks
import com.nei.cronos.utils.differenceParse
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
        onActionClick = viewModel::addEvent,
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
    onActionClick: (ChronometerUi, EventType) -> Unit = { _, _ -> },
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
            verticalArrangement = Arrangement.Center
        ) {
            when (state) {
                UiState.Error -> Text(text = "Error")
                UiState.Loading -> NeiLoading()
                is UiState.Success -> {
                    Body(
                        onEditChronometerClick = { onEditChronometerChange.invoke(true) },
                        onEditFormatClick = { onEditFormatChange.invoke(true) },
                        onActionClick = onActionClick,
                        onDeleteClick = onDeleteClick,
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
private fun ColumnScope.Body(
    onEditChronometerClick: () -> Unit,
    onEditFormatClick: () -> Unit,
    onActionClick: (ChronometerUi, EventType) -> Unit,
    onDeleteClick: () -> Unit,
    state: UiState.Success
) {
    val locale = getLocale()

    ChronometerChip(
        text = differenceParse(
            format = state.chronometer.format,
            locale = locale,
            startInclusive = state.timeRanges.first,
            endExclusive = state.timeRanges.second
        )
    )

    Text(text = state.chronometer.title)

    FlowRow(
        modifier = Modifier.padding(vertical = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilledTonalIconButton(
            onClick = onEditChronometerClick,
            modifier = Modifier.size(100.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
        }
        FilledTonalIconButton(
            onClick = onEditFormatClick,
            modifier = Modifier.size(100.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(imageVector = Icons.Rounded.Circle, contentDescription = null)
        }
    }

    Spacer(modifier = Modifier.weight(1f))

    FlowRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (!state.chronometer.isPaused) {
            Column {
                NeiIconButton(
                    iconVector = Icons.Rounded.Stop,
                    contentDescription = "Stop",
                    label = { Text("Stop") }
                ) {
                    onActionClick(state.chronometer, EventType.STOP)
                }
            }
        } else {
            NeiIconButton(
                iconVector = Icons.Rounded.RestartAlt,
                contentDescription = "restart",
                label = { Text("Restart") }
            ) {
                onActionClick(state.chronometer, EventType.RESTART)
            }
        }
        NeiIconButton(
            iconVector = Icons.Rounded.Flag,
            contentDescription = "New Lap",
            label = { Text("New Lap") }
        ) {
            onActionClick(state.chronometer, EventType.LAP)
        }
        NeiIconButton(
            iconVector = Icons.Rounded.Delete,
            contentDescription = "Delete",
            label = { Text("Delete") },
            onClick = onDeleteClick
        )
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