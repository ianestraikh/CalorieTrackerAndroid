package com.iest0002.calorietracker.data;

import java.util.Date;

public class Report {

    private int reportId;
    private Date reportDate;
    private double caloriesConsumed;
    private double caloriesBurned;
    private int stepsTaken;
    private double calorieGoal;
    private int userId;

    public Report(int reportId, Date reportDate, double caloriesConsumed, double caloriesBurned,
                  int stepsTaken, double calorieGoal, int userId) {
        this.reportId = reportId;
        this.reportDate = reportDate;
        this.caloriesConsumed = caloriesConsumed;
        this.caloriesBurned = caloriesBurned;
        this.stepsTaken = stepsTaken;
        this.calorieGoal = calorieGoal;
        this.userId = userId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public double getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public void setCaloriesConsumed(double caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public double getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(double calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
