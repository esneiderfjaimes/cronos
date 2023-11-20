package com.nei.cronos.utils

import com.nei.cronos.core.database.models.ChronometerFormat
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale

class ChronometerStringUtilsTest {
    @Test
    fun allFormats() {
        val locale = Locale.getDefault()
        val time = ZonedDateTime.of(2015, 9, 2, 0, 0, 0, 0, ZoneId.systemDefault())
        val formats = (0..255).map { ChronometerFormat.fromFlags(it) }
        val flagsStrings = formats.map { it.toFlags().toString(2) }
        val stringTimes = formats.map { time.differenceParse(it, locale) }
        println(flagsStrings.joinToString())
        println(stringTimes.joinToString())
    }
}