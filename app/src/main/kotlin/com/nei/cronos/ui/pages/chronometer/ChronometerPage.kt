@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.ui.pages.chronometer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.material3.TextButton
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
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.feature.editchronometer.EditChronometerDialog
import com.nei.cronos.ui.pages.format.EditFormat
import com.nei.cronos.utils.Mocks
import cronos.core.model.ChronometerFormat
import cronos.core.model.EventType

@Composable
fun ChronometerRoute(
    onBackClick: () -> Unit,
    viewModel: ChronometerViewModel = hiltViewModel(),
) {
    val state: ChronometerUiState by viewModel.state.collectAsStateWithLifecycle()
    val time: String by viewModel.currentTime.collectAsStateWithLifecycle()
    ChronometerScreen(
        state = state,
        timeSection = {
            ChronometerChip(
                text = time,
                modifier = Modifier.padding(16.dp)
            )
        },
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSaveClick,
        onNewLapClick = viewModel::onNewLapClick,
        updateFormat = viewModel::updateFormat,
        onDeleteClick = {
            viewModel.deleteChronometer()
            onBackClick.invoke()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun ChronometerScreen(
    state: ChronometerUiState,
    timeSection: @Composable () -> Unit = { },
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNewLapClick: (ChronometerUi, EventType) -> Unit = { _, _ -> },
    updateFormat: (ChronometerFormat) -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    var showEditChronometerDialog by rememberSaveable { mutableStateOf(false) }

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
            }, actions = {
                if (state is ChronometerUiState.Success) {
                    TextButton(onClick = {
                        onSaveClick.invoke()
                        onBackClick.invoke()
                    }, enabled = state.enabledSaveButton) {
                        Text(text = "Save")
                    }
                }
            })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(
                    ScrollState(0)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (state) {
                ChronometerUiState.Error -> Text(text = "Error")
                ChronometerUiState.Loading -> NeiLoading()
                is ChronometerUiState.Success -> {
                    timeSection.invoke()
                    ChronometerBody(
                        chronometer = state.chronometer,
                        onEditChronometerClick = {
                            showEditChronometerDialog = true
                        },
                        updateFormat = updateFormat
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    FlowRow(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (!state.isPaused) {
                            Column {
                                NeiIconButton(
                                    iconVector = Icons.Rounded.Stop,
                                    contentDescription = "Stop",
                                    label = { Text("Stop") }
                                ) {
                                    onNewLapClick.invoke(state.chronometer, EventType.STOP)
                                }
                            }
                        } else {
                            NeiIconButton(
                                iconVector = Icons.Rounded.RestartAlt,
                                contentDescription = "restart",
                                label = { Text("Restart") }
                            ) {
                                onNewLapClick.invoke(state.chronometer, EventType.RESTART)
                            }
                        }
                        NeiIconButton(
                            iconVector = Icons.Rounded.Flag,
                            contentDescription = "New Lap",
                            label = { Text("New Lap") }
                        ) {
                            onNewLapClick.invoke(state.chronometer, EventType.LAP)
                        }
                        NeiIconButton(
                            iconVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            label = { Text("Delete") },
                            onClick = onDeleteClick
                        )
                    }
                }
            }
        }
    }

    if (state is ChronometerUiState.Success) {
        if (showEditChronometerDialog) {
            EditChronometerDialog(
                chronometerId = state.chronometer.id,
                title = state.chronometer.title,
                onOpenBottomSheetChange = { showEditChronometerDialog = it }
            )
        }
    }
}

@Composable
fun ChronometerBody(
    chronometer: ChronometerUi,
    onEditChronometerClick: () -> Unit = {},
    updateFormat: (ChronometerFormat) -> Unit = {},
) {
    Text(text = chronometer.title)
    Spacer(modifier = Modifier.height(32.dp))
    FilledTonalIconButton(
        onClick = onEditChronometerClick,
        modifier = Modifier.size(64.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
    }

    Spacer(modifier = Modifier.height(32.dp))
    Text(text = "Format", style = MaterialTheme.typography.titleMedium)
    EditFormat(
        formatProvider = { chronometer.format },
        onUpdate = {
            updateFormat.invoke(it)
        },
        modifier = Modifier
            .padding(horizontal = 16.dp),
    )
}

@ThemePreviews
@Composable
private fun ChronometerPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            ChronometerScreen(
                state = ChronometerUiState.Success(
                    Mocks.chronometerPreview
                ),
                onBackClick = {},
                onSaveClick = {},
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
                state = ChronometerUiState.Success(
                    Mocks.chronometerPreview.copy(
                        isActive = false
                    )
                ),
                onBackClick = {},
                onSaveClick = {},
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
                state = ChronometerUiState.Loading,
                onBackClick = {},
                onSaveClick = {},
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
                state = ChronometerUiState.Error,
                onBackClick = {},
                onSaveClick = {},
            )
        }
    }
}