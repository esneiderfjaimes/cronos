package com.nei.cronos.core.designsystem.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview annotation that represents light and dark themes. Add this annotation to a
 * composable to render the both themes.
 */
 @Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme", device = Devices.TABLET,)
 @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
 /*@Preview(
     device = Devices.PHONE
 )
 @Preview(
     device = Devices.FOLDABLE,
     showSystemUi = true
 )
 @Preview(
     device = Devices.TABLET,
     showSystemUi = true
 )
 @Preview(
     device = Devices.DESKTOP,
     showSystemUi = true
 )*/
annotation class ThemePreviews
