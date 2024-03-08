package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * The main background for the app.
 * Uses [LocalAbsoluteTonalElevation] to set elevation of a [Surface].
 *
 * @param modifier Modifier to be applied to the background.
 * @param content The background content.
 */
@Composable
fun CronosBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            Box { content(this) }
        }
    }
}
