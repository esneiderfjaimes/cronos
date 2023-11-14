package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.nei.cronos.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronosScaffold(
    drawerState: DrawerState,
    drawerContent: @Composable () -> Unit,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    modalBottomSheetContent: @Composable() (ColumnScope.() -> Unit),
    openBottomSheet: Boolean,
    onOpenBottomSheetChange: (Boolean) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawerScaffold(
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text("show menu drawer")
                    }
                },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        Icons.Rounded.Menu,
                        contentDescription = "show menu drawer"
                    )
                }
            }
        },
        drawerState = drawerState,
        drawerContent = drawerContent,
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
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add chronometer"
                    )
                }
            }
        },
        content = content
    )

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