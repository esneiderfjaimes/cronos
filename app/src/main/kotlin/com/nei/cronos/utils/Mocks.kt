package com.nei.cronos.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.nei.cronos.R
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.domain.models.ChronometerUi
import com.nei.cronos.domain.models.SectionUi

object Mocks {

    val previewSections
        @Composable
        @ReadOnlyComposable
        get() = listOf(
            SectionUi(
                id = -1,
                name = "default",
                orderChronometerIds = listOf(),
                chronometers = listOf(
                    chronometerPreview
                )
            )
        )

    val chronometerPreview: ChronometerUi
        @Composable
        @ReadOnlyComposable
        get() = ChronometerUi(
            title = stringResource(R.string.first_chronometer_title)
        )

    fun getFirstChronometer(context: Context): ChronometerEntity {
        val title = context.resources.getString(R.string.first_chronometer_title)
        return ChronometerEntity(
            title = title
        )
    }

}