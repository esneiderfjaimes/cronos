package com.nei.cronos.core.database.models

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ChronometerFormatTest {
    @Test
    fun testToFlags() {
        val format = ChronometerFormat(
            showDay = true,
            showHour = false,
            showMinute = true,
            showSecond = false
        )
        assertEquals(10, format.toFlags())
    }

    @Test
    fun testFromFlags() {
        val flags = 13 // Binary: 1101
        val format = ChronometerFormat.fromFlags(flags)
        assertEquals(true, format.showDay)
        assertEquals(true, format.showHour)
        assertEquals(false, format.showMinute)
        assertEquals(true, format.showSecond)
    }

    @Test
    fun testDefaultFormat() {
        val defaultFormat = ChronometerFormat.DefaultFormat
        assertEquals(15, defaultFormat.toFlags()) // Binary: 0001111
        assertEquals("1111", defaultFormat.toFlags().toString(2))
    }

}