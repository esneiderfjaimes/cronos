package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawerScaffold(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    drawerState: DrawerState,
    drawerContent: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = drawerContent,
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = title,
                        navigationIcon = navigationIcon
                    )
                },
                floatingActionButton = floatingActionButton,
                content = content
            )
        }
    )
}