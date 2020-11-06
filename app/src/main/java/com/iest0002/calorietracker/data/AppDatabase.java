package com.iest0002.calorietracker.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;


@Database(entities = {Steps.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDatabase").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract StepsDao stepsDao();

}
