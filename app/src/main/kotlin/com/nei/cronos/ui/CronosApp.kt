package com.nei.cronos.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nei.cronos.core.designsystem.component.CronosModalDrawerSheet
import com.nei.cronos.core.designsystem.component.CronosScaffold
import com.nei.cronos.core.pages.AddChronometerPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronosApp() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val bottomSheetState = rememberModalBottomSheetState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    CronosScaffold(
        drawerState = drawerState,
        drawerContent = { CronosModalDrawerSheet(drawerState) },
        bottomSheetState = bottomSheetState,
        modalBottomSheetContent = {
            Column {
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