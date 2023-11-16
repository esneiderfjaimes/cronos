@file:OptIn(ExperimentalFoundationApi::class)

package com.nei.cronos.ui.contents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.DrawerState
import com.nei.cronos.core.designsystem.component.DrawerValue
import com.nei.cronos.core.designsystem.component.rememberDrawerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CronosDrawerContent(
    drawerState: DrawerState = rememberDrawerState()
) {
    val scope = rememberCoroutineScope()
    repeat(3) {
        NavigationDrawerItem(
            icon = { Icon(Icons.Rounded.Favorite, contentDescription = null) },
            label = { Text("Module in construction. \uD83D\uDEA7") },
            selected = it == 0,
            onClick = { scope.launch { drawerState.animateTo(DrawerValue.Hide) } },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                unselectedContainerColor = Color.Transparent,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}

@ThemePreviews
@Composable
fun CronosModalDrawerSheetPreview() {
    CronosTheme {
        CronosBackground(modifier = Modifier.fillMaxWidth()) {
            Column {
                CronosDrawerContent()
            }
        }
    }
}