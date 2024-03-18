package com.nei.cronos.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.nei.cronos.R
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.domain.models.SectionUi
import cronos.core.model.DarkThemeConfig
import cronos.core.model.SettingsState
import java.time.ZonedDateTime

object Mocks {

    val settingsUiStateSuccess: SettingsState
        @Composable
        @ReadOnlyComposable
        get() = SettingsState(
            darkThemeConfig = DarkThemeConfig.SYSTEM,
            useDynamicColor = true
        )

    val previewSections
        @Composable
        @ReadOnlyComposable
        get() = listOf(
            SectionUi(
                id = -1,
                name = "default",
                orderChronometerIds = listOf(),
                chronometers = listOf(
                    chronometerPreview
                )
            )
        )

    val chronometerPreview: ChronometerUi
        @Composable
        @ReadOnlyComposable
        get() = ZonedDateTime.now().let { time ->
            ChronometerUi(
                title = stringResource(R.string.first_chronometer_title),
                createdAt = time,
                startDate = time,
                lastTimeRunning = time,
            )
        }

}