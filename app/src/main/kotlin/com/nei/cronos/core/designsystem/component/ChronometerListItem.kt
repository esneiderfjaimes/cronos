package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.database.models.ChronometerFormat
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.utils.getLocale
import com.nei.cronos.utils.differenceParse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@Composable
fun ChronometerListItem(
    time: LocalDateTime,
    title: String,
    format: ChronometerFormat = ChronometerFormat(),
) {
    val locale = getLocale()
    var label by rememberSaveable { mutableStateOf(time.differenceParse(format, locale)) }
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            while (true) {
                delay(1000)
                label = time.differenceParse(format, locale)
            }
        }
    }

    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChronometerChip(label)
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
                    time = LocalDateTime.now(),
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
                        time = LocalDateTime.of(2015, 9, 2, 0, 0, 0),
                        title = "avocado 🥑",
                        format = format
                    )
                }
            }
        }
    }
}
