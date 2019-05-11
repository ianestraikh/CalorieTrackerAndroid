package com.iest0002.calorietracker.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * ref: https://developer.android.com/training/data-storage/room/referencing-data
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}