package com.nei.cronos.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nei.cronos.core.database.ChronometerEntity
import com.nei.cronos.core.designsystem.component.ChronometerChip

@Composable
fun HomeContent(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    HomeContent(state, paddingValues)
}

@Composable
private fun HomeContent(
    state: HomeViewModel.HomeState,
    paddingValues: PaddingValues = PaddingValues(),
) {
    if (state.isLoading) {
        LoadingContent(paddingValues)
        return
    }

    if (state.chronometers.isEmpty()) {
        EmptyChronometersContent(paddingValues)
        return
    }

    ChronometersContent(state.chronometers, paddingValues)
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun EmptyChronometersContent(paddingValues: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) { Text(text = "No chronometers") }
}

@Preview
@Composable
private fun ChronometersContent(
    chronometers: List<ChronometerEntity> = emptyList(),
    paddingValues: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
            .fillMaxSize(),
        contentPadding = paddingValues
    ) {
        items(chronometers) { chronometer ->
            ChronometerChip(time = chronometer.fromDate, title = chronometer.title)
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    HomeContent(HomeViewModel.HomeState())
}