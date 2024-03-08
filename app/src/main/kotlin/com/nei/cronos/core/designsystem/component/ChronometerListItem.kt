package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.model.ChronometerFormat
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun ChronometerListItem(
    time: ZonedDateTime,
    title: String,
    format: ChronometerFormat = ChronometerFormat(),
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChronometerChipRunning(time = time, format = format)
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
    HorizontalDivider()
}

@ThemePreviews
@Composable
private fun ChronometerListItemPreview() {
    CronosTheme {
        CronosBackground {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ChronometerListItem(
                    time = ZonedDateTime.now(),
                    title = "since this compiled",
                    format = ChronometerFormat.DefaultFormat
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun AllCombinations() {
    CronosTheme {
        CronosBackground {
            LazyColumn {
                this.items((0..255).toList()) {
                    val format = ChronometerFormat.fromFlags(it)
                    ChronometerListItem(
                        time = ZonedDateTime.of(2015, 9, 2, 0, 0, 0, 0, ZoneId.systemDefault()),
                        title = "avocado 🥑",
                        format = format
                    )
                }
            }
        }
    }
}
