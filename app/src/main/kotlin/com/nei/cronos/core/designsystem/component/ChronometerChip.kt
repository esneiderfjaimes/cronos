package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.utils.ChronometerStringUtils.differenceParse
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun ChronometerChip(time: LocalDateTime, title: String) {
    var label by rememberSaveable { mutableStateOf(time.differenceParse()) }
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000)
            label = time.differenceParse()
        }
    }

    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(25)
                )
                .padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = title, style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
    HorizontalDivider()
}

@ThemePreviews
@Composable
private fun ChronometerChipPreview() {
    CronosTheme {
        CronosBackground {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ChronometerChip(LocalDateTime.now(), "since this compiled")
                ChronometerChip(LocalDateTime.of(2023, 4, 14, 20, 45), "since my last birthday")
                ChronometerChip(LocalDateTime.of(2001, 4, 14, 20, 45), "since I was born")
            }
        }
    }
}