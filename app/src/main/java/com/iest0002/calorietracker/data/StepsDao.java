package com.iest0002.calorietracker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepsDao {
    @Query("SELECT * FROM steps")
    List<Steps> getAll();

    @Query("SELECT COUNT() from steps")
    int count();

    @Query("SELECT date from steps")
    List<String> getDate();

    @Query("SELECT stepsAmount from steps")
    List<Integer> getStepsAmount();

    @Query("SELECT SUM(stepsAmount) from steps")
    int sumStepsAmount();

    @Insert
    void insert(Steps steps);

    @Delete
    void delete(Steps steps);

    @Update(onConflict = REPLACE)
    void update(Steps... steps);

    @Query("DELETE FROM steps")
    void deleteAll();
}
