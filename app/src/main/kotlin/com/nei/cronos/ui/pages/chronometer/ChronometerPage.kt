@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.ui.pages.chronometer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.cronos.core.designsystem.component.ChronometerChip
import com.nei.cronos.core.designsystem.component.ChronometerChipRunning
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.NeiIconButton
import com.nei.cronos.core.designsystem.component.NeiLoading
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.ui.pages.format.EditFormat
import com.nei.cronos.utils.Mocks

@Composable
fun ChronometerRoute(
    onBackClick: () -> Unit,
    viewModel: ChronometerViewModel = hiltViewModel(),
) {
    val state: ChronometerUiState by viewModel.state.collectAsStateWithLifecycle()
    val time by viewModel.currentTime.collectAsStateWithLifecycle()
    ChronometerScreen(
        state = state,
        time = time,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSaveClick,
        onUpdateChronometer = viewModel::onUpdateChronometer,
        onNewLapClick = viewModel::onNewLapClick
    )
}

@Composable
internal fun ChronometerScreen(
    state: ChronometerUiState,
    time: String = "",
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUpdateChronometer: (ChronometerUi) -> Unit = {},
    onNewLapClick: (ChronometerUi) -> Unit = {},
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Chronometer") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
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
                    ChronometerChip(
                        text = time.ifBlank { "😅" },
                        modifier = Modifier.padding(16.dp)
                    )
                    ChronometerBody(
                        chronometer = state.chronometer,
                        onUpdateChronometer = onUpdateChronometer
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NeiIconButton(iconVector = Icons.Rounded.Flag) {
                        onNewLapClick.invoke(state.chronometer)
                    }
                }
            }
        }
    }
}

@Composable
fun ChronometerBody(
    chronometer: ChronometerUi,
    onUpdateChronometer: (ChronometerUi) -> Unit,
) {
    Text(text = chronometer.title)
    Spacer(modifier = Modifier.height(32.dp))
    Text(text = "Format", style = MaterialTheme.typography.titleMedium)
    EditFormat(
        format = chronometer.format,
        onUpdate = {
            onUpdateChronometer.invoke(chronometer.copy(format = it))
        },
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@ThemePreviews
@Composable
private fun ChronometerPreview() {
    CronosTheme {
        MaterialTheme.typography.titleMedium

        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            ChronometerScreen(
                state = ChronometerUiState.Success(
                    Mocks.chronometerPreview
                ),
                onBackClick = {},
                onSaveClick = {}
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
                onSaveClick = {}
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
                onSaveClick = {}
            )
        }
    }
}