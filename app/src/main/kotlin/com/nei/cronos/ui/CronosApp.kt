@file:OptIn(
    ExperimentalFoundationApi::class,
)

package com.nei.cronos.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nei.cronos.core.designsystem.component.drawer.DrawerState
import com.nei.cronos.core.designsystem.component.drawer.DrawerValue
import com.nei.cronos.core.designsystem.component.drawer.rememberDrawerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.feature.settings.navigation.navigateToSettings
import com.nei.cronos.feature.settings.navigation.settingsScreen
import com.nei.cronos.ui.pages.chronometer.navigation.chronometerScreen
import com.nei.cronos.ui.pages.chronometer.navigation.navigateToChronometer
import com.nei.cronos.ui.pages.home.navigation.HOME_ROUTE
import com.nei.cronos.ui.pages.home.navigation.homeScreen

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
            onChronometerClick = navController::navigateToChronometer,
            onSettingsClick = navController::navigateToSettings
        )

        settingsScreen(
            onBackClick = navController::popBackStack
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