package com.iest0002.calorietracker.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT COUNT() from user")
    int count();

    @Query("SELECT fname from user")
    List<String> getFirstName();

    @Query("SELECT lname from user")
    List<String> getLastName();

    @Query("SELECT email from user")
    List<String> getEmail();

    @Insert
    void insertAll(User... users);

    @Insert
    long insert(User user);

    @Delete
    void delete(User user);

    @Update(onConflict = REPLACE)
    void updateUsers(User... users);

    @Query("DELETE FROM user")
    void deleteAll();
}
