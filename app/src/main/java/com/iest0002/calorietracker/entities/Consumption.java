package com.iest0002.calorietracker.entities;

import java.util.Date;

public class Consumption {

    private int consumId;
    private Date consumptionDate;
    private int quantity;
    private int foodId;
    private int userId;

    public Consumption(int consumId, Date consumptionDate, int quantity, int foodId, int userId) {
        this.consumId = consumId;
        this.consumptionDate = consumptionDate;
        this.quantity = quantity;
        this.foodId = foodId;
        this.userId = userId;
    }

    public int getConsumId() {
        return consumId;
    }

    public void setConsumId(int consumId) {
        this.consumId = consumId;
    }

    public Date getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(Date consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
