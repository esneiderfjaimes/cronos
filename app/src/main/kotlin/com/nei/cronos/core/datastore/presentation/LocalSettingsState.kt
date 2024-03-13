package com.nei.cronos.core.datastore.presentation

import androidx.compose.runtime.compositionLocalOf
import cronos.core.model.SettingsState

val LocalSettingsState = compositionLocalOf<SettingsState> { error("SettingsState not present") }
