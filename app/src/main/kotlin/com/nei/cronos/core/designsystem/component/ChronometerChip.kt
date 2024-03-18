package com.nei.cronos.core.designsystem.component

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.utils.getLocale
import com.nei.cronos.utils.differenceParse
import cronos.core.model.ChronometerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun ChronometerChipRunning(
    time: ZonedDateTime,
    modifier: Modifier = Modifier,
    format: ChronometerFormat = ChronometerFormat(),
) {
    val locale = getLocale()
    var label by rememberSaveable(time, format, locale) {
        mutableStateOf(time.differenceParse(format, locale))
    }
    LaunchedEffect(time, format, locale) {
        withContext(Dispatchers.IO) {
            while (true) {
                delay(1000)
                label = time.differenceParse(format, locale)
            }
        }
    }
    ChronometerChip(text = label, modifier = modifier)
}

@Composable
fun ChronometerChip(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(25)
            )
            .padding(8.dp)
            .animateContentSize(),
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun ChronometerChip(text: String, brush: Brush) {
    Text(
        text = text,
        modifier = Modifier
            .background(
                brush = brush,
                shape = RoundedCornerShape(25)
            )
            .padding(8.dp)
            .animateContentSize(),
        color = Color.Black,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun ChronometerChipPreview() {
    val locale = getLocale()
    val time = ZonedDateTime.of(2015, 9, 2, 0, 0, 0, 0, ZoneId.systemDefault())
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
