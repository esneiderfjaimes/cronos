@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
)

package com.nei.cronos.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.LocalOwnersProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.nei.cronos.core.designsystem.component.drawer.DrawerState
import com.nei.cronos.core.designsystem.component.drawer.DrawerValue
import com.nei.cronos.core.designsystem.component.drawer.rememberDrawerState
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.feature.editchronometerformat.EditChronometerFormatDialog
import com.nei.cronos.feature.editchronometerformat.EditChronometerFormatViewModel
import com.nei.cronos.feature.settings.navigation.navigateToSettings
import com.nei.cronos.feature.settings.navigation.settingsScreen
import com.nei.cronos.ui.pages.chronometer.navigation.chronometerScreen
import com.nei.cronos.ui.pages.chronometer.navigation.navigateToChronometer
import com.nei.cronos.ui.pages.home.navigation.homeScreen
import com.nei.cronos.utils.Mocks

@SuppressLint("RestrictedApi")
@Composable
fun CronosApp(drawerState: DrawerState = rememberDrawerState()) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        navController = navController, startDestination = "test",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },

        ) {

        composable("test") {
            var show by rememberSaveable { mutableStateOf(false) }
            CronosTheme {
                Button(onClick = {
                    navController.navigate("edit_chronometer_dialog")
                    //   show = true
                }) {
                    Text(text = "Show dialog")
                }
            }
            if (show) {
                val navController2 = rememberNavController()
                navController2.createGraph("test") {
                    composable("test") {
                        Text(text = "test")
                    }
                }

                val saveableStateHolder = rememberSaveableStateHolder()
                navController2.currentBackStackEntry?.LocalOwnersProvider(saveableStateHolder) {

                    val viewModelStoreOwner = LocalViewModelStoreOwner.current
                    val mock = Mocks.chronometerPreview
                    val hiltViewModel = hiltViewModel(
                        viewModelStoreOwner!!,
                        creationCallback = { factory: EditChronometerFormatViewModel.Factory ->
                            factory.create(mock, null)
                        }
                    )
                    EditChronometerFormatDialog(
                        chronometer = mock,
                        lastEvent = null,
                        onOpenBottomSheetChange = {
                            val get =
                                viewModelStoreOwner.viewModelStore["androidx.lifecycle.ViewModelProvider.DefaultKey:com.nei.cronos.feature.editchronometerformat.EditChronometerFormatViewModel"]
                            viewModelStoreOwner.viewModelStore.clear()

                            show = false
                            // get?.viewModelScope?.coroutineContext?.cancel(CancellationException("ME LO MAMAN EN REVERAS"))
                            // navController.popBackStack()
                        },
                        viewModel = hiltViewModel
                    )

                    val joinToString =
                        viewModelStoreOwner?.viewModelStore?.keys()?.joinToString().also {
                            Log.i("TAG", "ChronometerScreen: $it")
                        }
                    Text(text = joinToString ?: "null")
                }
            }
        }

        dialog(
            route = "edit_chronometer_dialog",
            /*            dialogProperties = DialogProperties(
                            securePolicy = SecureFlagPolicy.SecureOn
                        )*/
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
                securePolicy = SecureFlagPolicy.SecureOn
            )
        ) {
            Box(modifier = Modifier.background(Color.Red).fillMaxSize())

            val viewModelStoreOwner = LocalViewModelStoreOwner.current
            val mock = Mocks.chronometerPreview
            val hiltViewModel = hiltViewModel(
                viewModelStoreOwner!!,
                creationCallback = { factory: EditChronometerFormatViewModel.Factory ->
                    factory.create(mock, null)
                }
            )
            EditChronometerFormatDialog(
                chronometer = mock,
                lastEvent = null,
                onOpenBottomSheetChange = {
                    // val get = viewModelStoreOwner?.viewModelStore?.get("androidx.lifecycle.ViewModelProvider.DefaultKey:com.nei.cronos.feature.editchronometerformat.EditChronometerFormatViewModel")

                    // get?.viewModelScope?.coroutineContext?.cancel(CancellationException("ME LO MAMAN EN REVERAS"))
                    navController.popBackStack()
                },
                viewModel = hiltViewModel
            )

            val joinToString =
                viewModelStoreOwner?.viewModelStore?.keys()?.joinToString().also {
                    Log.i("TAG", "ChronometerScreen: $it")
                }
            Text(text = joinToString ?: "null")
        }



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