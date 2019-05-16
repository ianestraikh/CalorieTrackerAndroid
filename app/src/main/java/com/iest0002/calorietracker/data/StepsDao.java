package com.iest0002.calorietracker.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

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

    @Query("UPDATE steps SET stepsAmount = :stepsAmount WHERE id = :id")
    void getLastName(int stepsAmount, int id);

    @Insert
    void insert(Steps steps);

    @Delete
    void delete(Steps steps);

    @Update(onConflict = REPLACE)
    void updateUsers(Steps... steps);

    @Query("DELETE FROM steps")
    void deleteAll();
}
