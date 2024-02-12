@file:OptIn(
    ExperimentalFoundationApi::class,
)

package com.nei.cronos.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nei.cronos.core.designsystem.component.DrawerState
import com.nei.cronos.core.designsystem.component.DrawerValue
import com.nei.cronos.core.designsystem.component.rememberDrawerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.ui.pages.home.navigation.HOME_ROUTE
import com.nei.cronos.ui.pages.home.navigation.homeScreen
import com.nei.cronos.ui.pages.chronometer.navigation.chronometerScreen
import com.nei.cronos.ui.pages.chronometer.navigation.navigateToChronometer

@Composable
fun CronosApp(drawerState: DrawerState = rememberDrawerState()) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        navController = navController, startDestination = HOME_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        homeScreen(
            drawerState = drawerState,
            onChronometerClick = navController::navigateToChronometer
        )

        chronometerScreen(
            onBackClick = navController::popBackStack
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