@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.cronos.ui.pages.chronometer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.core.designsystem.component.ChronometerChipRunning
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.NeiLoading
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.ui.pages.format.EditFormat

@Composable
fun ChronometerRoute(
    onBackClick: () -> Unit,
    viewModel: ChronometerViewModel = hiltViewModel(),
) {
    val state: ChronometerUiState by viewModel.state.collectAsStateWithLifecycle()
    ChronometerScreen(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSaveClick,
        onUpdateChronometer = viewModel::onUpdateChronometer
    )
}

@Composable
internal fun ChronometerScreen(
    state: ChronometerUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUpdateChronometer: (ChronometerEntity) -> Unit = {},
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state) {
                ChronometerUiState.Error -> item { Text(text = "Error") }
                ChronometerUiState.Loading -> item { NeiLoading() }
                is ChronometerUiState.Success -> item {
                    ChronometerBody(
                        chronometer = state.chronometer,
                        onUpdateChronometer = onUpdateChronometer
                    )
                }
            }
        }
    }
}

@Composable
fun ChronometerBody(
    chronometer: ChronometerEntity,
    onUpdateChronometer: (ChronometerEntity) -> Unit,
) {
    ChronometerChipRunning(
        time = chronometer.fromDate,
        format = chronometer.format,
        modifier = Modifier.padding(16.dp),
    )
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
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            ChronometerScreen(
                state = ChronometerUiState.Success(
                    ChronometerEntity(title = "since I've been using the app")
                ),
                onBackClick = {},
                onSaveClick = {}
            )
        }
    }
}