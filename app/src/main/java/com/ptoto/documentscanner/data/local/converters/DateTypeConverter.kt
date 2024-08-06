package com.ptoto.documentscanner.data.local.converters

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateTimestamp(date: Date): Long {
        return date.time
    }

}