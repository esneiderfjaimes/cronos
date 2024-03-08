@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.nei.cronos.ui.contents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.nei.cronos.R
import com.nei.cronos.core.designsystem.component.DrawerState
import com.nei.cronos.core.designsystem.component.DrawerValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CronosTopAppBar(drawerState: DrawerState, scope: CoroutineScope = rememberCoroutineScope()) {
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
                    onClick = {
                        scope.launch {
                            drawerState.animateTo(
                                if (drawerState.currentValue == DrawerValue.Hide) DrawerValue.Show
                                else DrawerValue.Hide
                            )
                        }
                    }) {
                    Icon(
                        Icons.Rounded.Menu, contentDescription = "show menu drawer"
                    )
                }
            }
        },
    )
}