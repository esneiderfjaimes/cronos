@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.nei.cronos.ui.pages.chronometer

import androidx.compose.animation.Crossfade
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.PauseCircleOutline
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
import com.nei.cronos.core.designsystem.component.ChronometerChip
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.NeiLoading
import com.nei.cronos.core.designsystem.component.button.TonalIconButton
import com.nei.cronos.core.designsystem.dialog.BaseAlertDialog
import com.nei.cronos.core.designsystem.icons.TwoDots
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.utils.getLocale
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
    val locale = getLocale()
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val rangesState by viewModel.rangesState.collectAsStateWithLifecycle()
    var showEditAppearance by rememberSaveable { mutableStateOf(false) }
    var showEditFormat by rememberSaveable { mutableStateOf(false) }
    var showAlertDelete by rememberSaveable { mutableStateOf(false) }

    FlowAsState(flow = viewModel.events) { event ->
        when (event) {
            is Event.FinishUpdate -> {
                showEditFormat = false
            }
        }
    }

    ChronometerScreen(
        labelProvider = { it: ChronometerFormat ->
            differenceParse(
                format = it,
                locale = locale,
                startInclusive = rangesState.first,
                endExclusive = rangesState.second
            )
        },
        scrollState = scrollState,
        onBackClick = onBackClick,
        showEditAppearance = showEditAppearance,
        onEditChronometerChange = { showEditAppearance = it },
        showEditFormat = showEditFormat,
        onEditFormatChange = { showEditFormat = it },
        showAlertDelete = showAlertDelete,
        onAlertDeleteChange = { showAlertDelete = it },
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
    labelProvider: (ChronometerFormat) -> String = { "00:00" },
    onBackClick: () -> Unit = {},
    showEditAppearance: Boolean = false,
    onEditChronometerChange: (Boolean) -> Unit = {},
    showEditFormat: Boolean = false,
    onEditFormatChange: (Boolean) -> Unit = {},
    showAlertDelete: Boolean = false,
    onAlertDeleteChange: (Boolean) -> Unit = {},
    onConfirmationDiscardChanges: () -> Unit = {},
    onStopClick: () -> Unit = {},
    onRestartClick: () -> Unit = {},
    onNewLapClick: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    onUpdate: (ChronometerFormat) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    state: UiState,
    scrollState: ScrollState = rememberScrollState(),
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state) {
                UiState.Error -> Text(text = "Error")
                UiState.Loading -> NeiLoading()
                is UiState.Success -> {
                    Box {
                        ChronometerChip(
                            text = labelProvider.invoke(state.chronometer.format),
                            modifier = Modifier.padding(32.dp),
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 32.dp - 12.dp, top = 32.dp - 12.dp)
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = state.chronometer.isPaused,
                                label = "icon_animation2",
                                enter = scaleIn(),
                                exit = scaleOut()
                            ) {
                                Icon(
                                    Icons.Rounded.PauseCircleOutline,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = CircleShape
                                        ),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    Body(
                        onEditChronometerClick = { onEditChronometerChange.invoke(true) },
                        onEditFormatClick = { onEditFormatChange.invoke(true) },
                        onDeleteClick = { onAlertDeleteChange.invoke(true) },
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
                timeProvider = labelProvider,
                onConfirmation = onConfirmation,
                onUpdate = onUpdate
            )
        }
        if (showAlertDelete) {
            BaseAlertDialog(
                title = "Delete chronometer?",
                text = "Are you sure you want to delete this chronometer?",
                confirmButtonText = "Delete",
                onDismissRequest = { onAlertDeleteChange.invoke(false) },
                onConfirmation = {
                    onDeleteClick.invoke()
                    onAlertDeleteChange.invoke(false)
                },
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
            Icon(
                imageVector = Icons.Rounded.Edit, contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
        TonalIconButton(
            onClick = onEditFormatClick,
            label = "Format",
        ) {
            Icon(
                imageVector = Icons.Rounded.TwoDots, contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
        TonalIconButton(
            onClick = { onDeleteClick.invoke() },
            label = "Delete",
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }

        Text(
            text = "Actions",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(4.dp),
            textAlign = TextAlign.Center
        )
        TonalIconButton(
            onClick = {
                if (isPaused) {
                    onRestartClick.invoke()
                } else {
                    onStopClick.invoke()
                }
            },
            label = if (isPaused) "Restart" else "Stop",
        ) {
            Crossfade(targetState = isPaused, label = "stop/restart_animation") {
                Icon(
                    imageVector = if (it) Icons.Rounded.RestartAlt else Icons.Rounded.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        TonalIconButton(
            onClick = { onNewLapClick.invoke() },
            label = "New Lap",
        ) {
            Icon(
                imageVector = Icons.Rounded.Flag, contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
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
                showEditFormat = true,
                state = UiState.Success(
                    Mocks.chronometerPreview.copy(isActive = false)
                ),
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