package com.iest0002.calorietracker.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Steps {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int stepsAmount;
    private String date;

    public Steps(int stepsAmount, String date) {
        this.stepsAmount = stepsAmount;
        this.date = date;
    }

    public Steps(int stepsAmount, Date date) {
        this.stepsAmount = stepsAmount;
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.US);
        this.date = df.format(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStepsAmount() {
        return stepsAmount;
    }

    public void setStepsAmount(int stepsAmount) {
        this.stepsAmount = stepsAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(Date date) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.US);
        this.date = df.format(date);
    }
}
