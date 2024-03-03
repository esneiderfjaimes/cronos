@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.nei.cronos.ui.contents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.nei.cronos.core.designsystem.component.drawer.DrawerState
import com.nei.cronos.core.designsystem.component.drawer.DrawerValue
import com.nei.cronos.core.designsystem.component.drawer.NeiDrawer
import com.nei.cronos.core.designsystem.component.drawer.rememberDrawerState
import kotlinx.coroutines.launch

@Composable
fun CronosScaffold(
    drawerState: DrawerState = rememberDrawerState(),
    drawerContent: @Composable (ColumnScope.() -> Unit),
    openBottomSheet: Boolean,
    onOpenBottomSheetChange: (Boolean) -> Unit,
    modalBottomSheetContent: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    NeiDrawer(
        drawerState = drawerState,
        scope = scope,
        contentDrawer = drawerContent
    ) {
        Scaffold(
            modifier = Modifier.anchoredDraggable(
                state = drawerState,
                orientation = Orientation.Horizontal
            ),
            topBar = {
                CronosTopAppBar(
                    drawerState = drawerState,
                    scope = scope,
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

    BackHandler(drawerState.currentValue == DrawerValue.Show) {
        scope.launch {
            drawerState.animateTo(DrawerValue.Hide)
        }
    }

    // Sheet content
    if (openBottomSheet) {
        modalBottomSheetContent.invoke()
    }
}