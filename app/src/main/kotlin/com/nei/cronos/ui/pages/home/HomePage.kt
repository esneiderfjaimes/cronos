@file:OptIn(
    ExperimentalFoundationApi::class,
)

package com.nei.cronos.ui.pages.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nei.cronos.core.database.models.SectionEntity
import com.nei.cronos.core.designsystem.component.ChronometerListItem
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.DrawerState
import com.nei.cronos.core.designsystem.component.rememberDrawerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.domain.models.SectionUi
import com.nei.cronos.ui.contents.CronosDrawerContent
import com.nei.cronos.ui.contents.CronosScaffold
import com.nei.cronos.ui.pages.addchronometer.AddChronometerPage
import com.nei.cronos.utils.Mocks

typealias OnChronometerClick = (Long) -> Unit

@Composable
fun HomeRoute(
    drawerState: DrawerState,
    onChronometerClick: OnChronometerClick,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()
    HomeScreen(
        drawerState = drawerState,
        openBottomSheet = openBottomSheet,
        onOpenBottomSheetChange = { openBottomSheet = it },
        state = state,
        onChronometerClick = onChronometerClick
    )
}

@Composable
private fun HomeScreen(
    drawerState: DrawerState = rememberDrawerState(),
    openBottomSheet: Boolean = false,
    onOpenBottomSheetChange: (Boolean) -> Unit = {},
    state: HomeViewModel.HomeState,
    onChronometerClick: OnChronometerClick = {},
) {
    CronosScaffold(
        drawerState = drawerState,
        drawerContent = { CronosDrawerContent(drawerState) },
        openBottomSheet = openBottomSheet,
        onOpenBottomSheetChange = onOpenBottomSheetChange,
        modalBottomSheetContent = {
            AddChronometerPage(
                openBottomSheet = openBottomSheet,
                onOpenBottomSheetChange = onOpenBottomSheetChange,
            )
        },
    ) { paddingValues ->
        HomeContent(
            state = state,
            paddingValues = paddingValues,
            onChronometerClick = onChronometerClick
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeViewModel.HomeState,
    paddingValues: PaddingValues = PaddingValues(),
    onChronometerClick: OnChronometerClick = {},
) {
    if (state.isLoading) {
        LoadingContent(paddingValues)
        return
    }

    if (state.sections.isEmpty()) {
        EmptyChronometersContent(paddingValues)
        return
    }

    SectionsContent(
        sections = state.sections,
        paddingValues = paddingValues,
        onChronometerClick = onChronometerClick
    )
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

@Composable
private fun SectionsContent(
    sections: List<SectionUi> = emptyList(),
    paddingValues: PaddingValues = PaddingValues(),
    onChronometerClick: OnChronometerClick,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = paddingValues,
    ) {
        sections.forEach { section ->
            sectionContent(
                section = section,
                onChronometerClick = onChronometerClick
            )
        }
    }
}

private fun LazyListScope.sectionContent(
    section: SectionUi,
    onChronometerClick: OnChronometerClick
) {
    if (section.id != SectionEntity.NONE_SECTION_ID) {
        item(
            key = "h${section.id}",
            contentType = section.name
        ) {
            Text(text = section.name)
        }
    }
    items(
        items = section.chronometers,
        key = { "s${section.id}c${it.id}" }
    ) { chronometer ->
        ChronometerListItem(
            time = chronometer.fromDate,
            title = chronometer.title,
            format = chronometer.format
        ) {
            onChronometerClick.invoke(chronometer.id)
        }
    }
}

@ThemePreviews
@Composable
fun HomeScreenLoadingPreview() {
    CronosTheme {
        CronosBackground {
            HomeScreen(
                state = HomeViewModel.HomeState(isLoading = true),
            )
        }
    }
}

@ThemePreviews
@Composable
fun HomeScreenWithEmptyChronometersPreview() {
    CronosTheme {
        CronosBackground {
            HomeScreen(
                state = HomeViewModel.HomeState(sections = emptyList()),
            )
        }
    }
}

@ThemePreviews
@Composable
fun HomeScreenPreview() {
    CronosTheme {
        CronosBackground {
            HomeScreen(
                state = HomeViewModel.HomeState(
                    sections = Mocks.previewSections
                ),
            )
        }
    }
}
