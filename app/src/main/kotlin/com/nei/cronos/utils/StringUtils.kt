package com.nei.cronos.utils

import java.text.NumberFormat
import java.util.Locale

fun Number.format(locale: Locale): String {
    return NumberFormat.getNumberInstance(locale).format(this)
}