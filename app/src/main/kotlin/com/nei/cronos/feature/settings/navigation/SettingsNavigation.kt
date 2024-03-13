package com.nei.cronos.feature.settings.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nei.cronos.core.designsystem.navigation.Default
import com.nei.cronos.feature.settings.SettingsRoute

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings() {
    this.navigate(SETTINGS_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.settingsScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = SETTINGS_ROUTE,
        enterTransition = {
            fadeIn(
                spring(stiffness = Spring.Default)
            ) + scaleIn(
                animationSpec = spring(stiffness = Spring.Default),
                initialScale = 1.125f,
            )
        },
        exitTransition = {
            slideOutHorizontally(
                spring(
                    stiffness = Spring.Default,
                    visibilityThreshold = IntOffset.VisibilityThreshold
                ),
                targetOffsetX = { it }
            )
        }
    ) {
        SettingsRoute(onBackClick = onBackClick)
    }
}
