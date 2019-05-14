package com.iest0002.calorietracker.data;

public class Food {

    private Integer foodId;
    private String foodName;
    private String foodCategory;
    private double calorieAmount;
    private String servingUnit;
    private double servingAmount;
    private double fat;


    public Food(Integer foodId, String foodName, String foodCategory, double calorieAmount,
                String servingUnit, double servingAmount, double fat) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.calorieAmount = calorieAmount;
        this.servingUnit = servingUnit;
        this.servingAmount = servingAmount;
        this.fat = fat;
    }

    public Food(String foodName, String foodCategory, double calorieAmount,
                String servingUnit, double servingAmount, double fat) {
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.calorieAmount = calorieAmount;
        this.servingUnit = servingUnit;
        this.servingAmount = servingAmount;
        this.fat = fat;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public double getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(double calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public double getServingAmount() {
        return servingAmount;
    }

    public void setServingAmount(double servingAmount) {
        this.servingAmount = servingAmount;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    @Override
    public String toString() {
        return this.foodName;
    }
}
