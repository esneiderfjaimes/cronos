package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.database.models.ChronometerFormat
import com.nei.cronos.core.designsystem.utils.getLocale
import com.nei.cronos.utils.differenceParse
import java.time.LocalDateTime

@Composable
fun ChronometerChip(text: String) {
    Text(
        text = text,
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
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun ChronometerChipPreview() {
    val locale = getLocale()
    val time = LocalDateTime.of(2015, 9, 2, 0, 0, 0)
    FlowRow(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        (0..255).forEach {
            val format = ChronometerFormat.fromFlags(it)
            ChronometerChip(time.differenceParse(format, locale))
        }
    }
}
