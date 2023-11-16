package com.nei.cronos.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeContent(
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier
) {
    val state by viewModel.state.collectAsState()
    HomeContent(state, modifier)
}

@Composable
private fun HomeContent(
    state: HomeViewModel.HomeState,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        return
    }
    LazyColumn(modifier) {
        items(state.chronometers) {
            Text(text = it.toString())
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    HomeContent(HomeViewModel.HomeState())
}