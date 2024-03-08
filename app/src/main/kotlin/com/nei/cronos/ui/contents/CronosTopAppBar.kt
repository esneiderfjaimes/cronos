@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.nei.cronos.ui.contents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nei.cronos.R
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.drawer.DrawerState
import com.nei.cronos.core.designsystem.component.drawer.DrawerValue
import com.nei.cronos.core.designsystem.component.drawer.rememberDrawerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CronosTopAppBar(
    drawerState: DrawerState = rememberDrawerState(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val isDrawerOpen by remember { derivedStateOf { drawerState.currentValue == DrawerValue.Show } }
    val isDrawerHideAndStatic by remember { derivedStateOf { drawerState.requireOffset() == 0f } }

    CenterAlignedTopAppBar(
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
                IconButton(
                    enabled = isDrawerHideAndStatic,
                    onClick = {
                        scope.launch {
                            drawerState.animateTo(
                                if (!isDrawerOpen) DrawerValue.Show
                                else DrawerValue.Hide
                            )
                        }
                    }) {
                    Icon(
                        if (drawerState.targetValue != DrawerValue.Show) Icons.Rounded.Menu
                        else Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = "show menu drawer"
                    )
                }
            }
        },
    )
}

@ThemePreviews
@Composable
fun CronosTopAppBarPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            Column {
                CronosTopAppBar()
            }
        }
    }
}

@ThemePreviews
@Composable
fun CronosTopAppBarPreview2() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            Column {
                CronosTopAppBar(
                    drawerState = rememberDrawerState(DrawerValue.Show)
                )
            }
        }
    }
}