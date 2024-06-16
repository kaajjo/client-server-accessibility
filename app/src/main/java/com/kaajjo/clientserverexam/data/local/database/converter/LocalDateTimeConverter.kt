package com.kaajjo.clientserverexam.data.local.database.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * Converts LocalDateTime
 */
class LocalDateTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) {
            null
        } else {
            LocalDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}