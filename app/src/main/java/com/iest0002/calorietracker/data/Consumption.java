package com.iest0002.calorietracker.data;

import java.util.Date;

public class Consumption {

    private Integer consumId;
    private Date consumptionDate;
    private int quantity;
    private Food foodId;
    private User userId;

    public Consumption(Date consumptionDate, int quantity, Food foodId, User userId) {
        this.consumptionDate = consumptionDate;
        this.quantity = quantity;
        this.foodId = foodId;
        this.userId = userId;
    }

    public Integer getConsumId() {
        return consumId;
    }

    public void setConsumId(Integer consumId) {
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

    public Food getFoodId() {
        return foodId;
    }

    public void setFoodId(Food foodId) {
        this.foodId = foodId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
