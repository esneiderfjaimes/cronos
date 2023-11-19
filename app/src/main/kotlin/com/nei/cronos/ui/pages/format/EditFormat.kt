package com.nei.cronos.ui.pages.format

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
    format: ChronometerFormat,
    onUpdate: (ChronometerFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(modifier) {
        ChronometerFlag(
            text = "Years",
            checked = format.showYear,
            onCheckedChange = { onUpdate(format.copy(showYear = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Months",
            checked = format.showMonth,
            onCheckedChange = { onUpdate(format.copy(showMonth = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Weeks",
            checked = format.showWeek,
            onCheckedChange = { onUpdate(format.copy(showWeek = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Days",
            checked = format.showDay,
            onCheckedChange = { onUpdate(format.copy(showDay = it)) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.fillMaxWidth())
        ChronometerFlag(
            text = "Hours",
            checked = format.showHour,
            onCheckedChange = { onUpdate(format.copy(showHour = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Minutes",
            checked = format.showMinute,
            onCheckedChange = { onUpdate(format.copy(showMinute = it)) },
            modifier = Modifier.weight(1f)
        )
        ChronometerFlag(
            text = "Seconds",
            checked = format.showSecond,
            onCheckedChange = { onUpdate(format.copy(showSecond = it)) },
            modifier = Modifier.weight(1f)
        )
        SwitchFormat(
            text = "Hide counters equal to zero",
            checked = format.hideZeros,
            onCheckedChange = { onUpdate(format.copy(hideZeros = it)) }
        )
    }
}

@ThemePreviews
@Composable
fun EditFormatPreview() {
    var format by remember { mutableStateOf(ChronometerFormat()) }
    CronosTheme {
        CronosBackground {
            EditFormat(format, onUpdate = { format = it })
        }
    }
}