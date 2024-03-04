package com.nei.cronos.ui.pages.format

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nei.cronos.core.database.models.ChronometerFormat
import com.nei.cronos.core.designsystem.component.ChronometerFlag
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.component.SwitchFormat
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditFormat(
    formatProvider: () -> ChronometerFormat,
    onUpdate: (ChronometerFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(modifier) {
        ChronometerFlag(
            text = "Years",
            checked = formatProvider().showYear,
            onCheckedChange = { onUpdate(formatProvider().copy(showYear = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Months",
            checked = formatProvider().showMonth,
            onCheckedChange = { onUpdate(formatProvider().copy(showMonth = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Weeks",
            checked = formatProvider().showWeek,
            onCheckedChange = { onUpdate(formatProvider().copy(showWeek = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Days",
            checked = formatProvider().showDay,
            onCheckedChange = { onUpdate(formatProvider().copy(showDay = it)) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.fillMaxWidth())
        ChronometerFlag(
            text = "Hours",
            checked = formatProvider().showHour,
            onCheckedChange = { onUpdate(formatProvider().copy(showHour = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Minutes",
            checked = formatProvider().showMinute,
            onCheckedChange = { onUpdate(formatProvider().copy(showMinute = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Seconds",
            checked = formatProvider().showSecond,
            onCheckedChange = { onUpdate(formatProvider().copy(showSecond = it)) },
            modifier = Modifier.weight(1f)
        )
        SwitchFormat(
            text = "Hide counters equal to zero",
            checked = formatProvider().hideZeros,
            onCheckedChange = { onUpdate(formatProvider().copy(hideZeros = it)) }
        )
        AnimatedVisibility(
            visible = formatProvider().timeFlagsEnabled,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            SwitchFormat(
                text = "Compact time",
                checked = formatProvider().compactTimeEnabled,
                onCheckedChange = { onUpdate(formatProvider().copy(compactTimeEnabled = it)) }
            )
        }
    }
}

@ThemePreviews
@Composable
fun EditFormatPreview() {
    var format by remember { mutableStateOf(ChronometerFormat()) }
    CronosTheme {
        CronosBackground {
            EditFormat(
                formatProvider = { format },
                onUpdate = { format = it }
            )
        }
    }
}