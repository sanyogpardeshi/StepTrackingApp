package com.example.ifit.IFitApp.Data.DataSource

import androidx.room.TypeConverter
import java.util.*

/* ROOM DATABASE CAN NOT HOLD SPECIAL DATA TYPES LIKE DATE */
/* BECAUSE OF THAT WE NEED CONVERTERS LIKE THIS ONE. */

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}