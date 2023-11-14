package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CronosModalDrawerSheet(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))

        repeat(3) {
            NavigationDrawerItem(
                icon = { Icon(Icons.Rounded.Favorite, contentDescription = null) },
                label = { Text("Module in construction. \uD83D\uDEA7") },
                selected = false,
                onClick = { scope.launch { drawerState.close() } },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(Modifier.weight(1f))
        Text(
            text = "Made by Nei \uD83C\uDDE8\uD83C\uDDF4",
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}