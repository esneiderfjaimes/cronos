package com.nei.cronos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.ui.CronosApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Disable splash screen
        installSplashScreen().setKeepOnScreenCondition { false }

        setContent {
            CronosTheme(
                dynamicColor = true
            ) {
                CronosApp()
            }
        }
    }
}