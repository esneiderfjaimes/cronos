@file:OptIn(ExperimentalFoundationApi::class)

package com.nei.cronos.ui.pages.home.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nei.cronos.core.designsystem.component.drawer.DrawerState
import com.nei.cronos.core.designsystem.navigation.Default
import com.nei.cronos.ui.pages.home.HomeRoute
import com.nei.cronos.ui.pages.home.OnChronometerClick

const val HOME_ROUTE = "home"

fun NavGraphBuilder.homeScreen(
    drawerState: DrawerState,
    onChronometerClick: OnChronometerClick,
) {
    composable(
        route = HOME_ROUTE,
        enterTransition = {
            slideInHorizontally(
                spring(
                    stiffness = Spring.Default,
                    visibilityThreshold = IntOffset.VisibilityThreshold
                ),
            ) + fadeIn(
                spring(stiffness = Spring.Default),
                0.5f
            )
        },
        exitTransition = {
            slideOutHorizontally(
                spring(
                    stiffness = Spring.Default,
                    visibilityThreshold = IntOffset.VisibilityThreshold
                ),
            ) + fadeOut(
                spring(stiffness = Spring.Default),
                0.5f
            )
        },
    ) {
        HomeRoute(drawerState = drawerState, onChronometerClick = onChronometerClick)
    }
}
