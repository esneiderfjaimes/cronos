@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.nei.cronos.ui.contents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import com.nei.cronos.core.designsystem.component.DrawerState
import com.nei.cronos.core.designsystem.component.NeiDrawer
import com.nei.cronos.core.designsystem.component.rememberDrawerState

@Composable
fun CronosScaffold(
    drawerState: DrawerState = rememberDrawerState(),
    drawerContent: @Composable BoxWithConstraintsScope.() -> Unit,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    modalBottomSheetContent: @Composable (ColumnScope.() -> Unit),
    openBottomSheet: Boolean,
    onOpenBottomSheetChange: (Boolean) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    NeiDrawer(
        drawerState = drawerState,
        contentDrawer = drawerContent
    ) {
        Scaffold(
            topBar = {
                CronosTopAppBar(
                    drawerState = drawerState,
                )
            },
            floatingActionButton = {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("Add new chronometer")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    FloatingActionButton(onClick = { onOpenBottomSheetChange(true) }) {
                        Icon(
                            imageVector = Icons.Rounded.Add, contentDescription = "Add chronometer"
                        )
                    }
                }
            },
            content = content
        )
    }

    // Sheet content
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onOpenBottomSheetChange(false) },
            sheetState = bottomSheetState,
            dragHandle = {},
            windowInsets = WindowInsets(0),
            content = modalBottomSheetContent,
        )
    }
}