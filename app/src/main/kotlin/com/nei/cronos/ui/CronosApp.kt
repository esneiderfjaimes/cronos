@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.nei.cronos.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.component.DrawerState
import com.nei.cronos.core.designsystem.component.DrawerValue
import com.nei.cronos.core.designsystem.component.rememberDrawerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.ui.contents.CronosModalDrawerSheet
import com.nei.cronos.ui.contents.CronosScaffold
import com.nei.cronos.ui.pages.AddChronometerPage

@Composable
fun CronosApp(drawerState: DrawerState = rememberDrawerState()) {
    val bottomSheetState = rememberModalBottomSheetState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    CronosScaffold(
        drawerState = drawerState,
        drawerContent = { CronosModalDrawerSheet(drawerState) },
        bottomSheetState = bottomSheetState,
        modalBottomSheetContent = {
            Column(Modifier.padding(horizontal = 8.dp)) {
                AddChronometerPage(
                    sheetState = bottomSheetState,
                    onOpenBottomSheetChange = { openBottomSheet = it },
                )
            }
        },
        openBottomSheet = openBottomSheet,
        onOpenBottomSheetChange = { openBottomSheet = it },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        )
    }
}

@Composable
@ThemePreviews
fun CronosAppPreview() {
    CronosTheme {
        CronosApp()
    }
}

@Composable
@ThemePreviews
fun CronosAppPreviewWithDrawer() {
    CronosTheme {
        CronosApp(rememberDrawerState(DrawerValue.Show))
    }
}