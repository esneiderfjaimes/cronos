package com.nei.cronos.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun CompositionLocalProviderFixedInsets(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalFixedInsets provides rememberFixedInsets(), content = content)
}

@Composable
fun rememberFixedInsets(): FixedInsets {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    return remember {
        FixedInsets(
            statusBarHeight = systemBarsPadding.calculateTopPadding(),
            navigationBarsPadding = PaddingValues(
                bottom = systemBarsPadding.calculateBottomPadding(),
                start = systemBarsPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = systemBarsPadding.calculateEndPadding(LayoutDirection.Ltr),
            ),
        )
    }
}

data class FixedInsets(
    val statusBarHeight: Dp = 0.dp,
    val navigationBarsPadding: PaddingValues = PaddingValues(),
)

val LocalFixedInsets = compositionLocalOf<FixedInsets> { error("no FixedInsets provided!") }

@Composable
fun Modifier.fixedStatusBarsPadding(): Modifier {
    return this.padding(top = LocalFixedInsets.current.statusBarHeight)
}

@Composable
fun Modifier.fixedNavigationBarsPadding(): Modifier {
    return this.padding(paddingValues = LocalFixedInsets.current.navigationBarsPadding)
}