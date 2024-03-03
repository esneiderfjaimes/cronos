package com.nei.cronos.core.designsystem.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview annotation that represents light and dark themes. Add this annotation to a
 * composable to render the both themes.
 */
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark theme - phone",
    group = "phone"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light theme - phone - landscape",
    device = "spec:width=411dp,height=891dp,orientation=landscape", group = "phone"
)
/*
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light theme - tablet",
    device = Devices.TABLET
)
*/
annotation class ThemePreviews
