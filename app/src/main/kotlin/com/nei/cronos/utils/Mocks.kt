package com.nei.cronos.utils

import android.content.Context
import com.nei.cronos.R
import com.nei.cronos.core.database.mappers.toUi
import com.nei.cronos.core.database.models.ChronometerEntity
import com.nei.cronos.domain.models.SectionUi

object Mocks {

    val previewSections
        get() = listOf(
            previewSection
        )

    val previewSection: SectionUi
        get() = SectionUi(
            id = -1,
            name = "default",
            orderChronometerIds = listOf(),
            chronometers = listOf(
                firstChronometer.toUi()
            )
        )

    val firstChronometer: ChronometerEntity
        get() = ChronometerEntity(
            title = "since I've been using the app",
        )

    fun getFirstChronometer(context: Context): ChronometerEntity {
        val title = context.resources.getString(R.string.first_chronometer_title)
        return ChronometerEntity(
            title = title
        )
    }

}