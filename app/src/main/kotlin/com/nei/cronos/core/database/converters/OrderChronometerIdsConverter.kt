package com.nei.cronos.core.database.converters

import androidx.room.TypeConverter

class OrderChronometerIdsConverter {

    @TypeConverter
    fun stringToOrderChronometerIds(orderChronometerIds: String): List<Long> {
        if (orderChronometerIds.isBlank()) return emptyList()
        return orderChronometerIds.split(",")
            .takeIf { it.isNotEmpty() }
            ?.map { it.toLong() }
            ?: emptyList()
    }

    @TypeConverter
    fun orderChronometerIdsToString(orderChronometerIds: List<Long>): String {
        return orderChronometerIds.joinToString(",")
    }
}