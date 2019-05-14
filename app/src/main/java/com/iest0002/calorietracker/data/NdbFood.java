package com.iest0002.calorietracker.data;

import java.util.ArrayList;

public class NdbFood {
    private List list;
    private ArrayList<Foods> foods;

    public NdbFood(List list, ArrayList<Foods> foods) {
        this.list = list;
        this.foods = foods;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public ArrayList<Foods> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Foods> foods) {
        this.foods = foods;
    }

    public static class List {
        private ArrayList<Item> item;

        public List(ArrayList<Item> item) {
            this.item = item;
        }

        public ArrayList<Item> getItem() {
            return item;
        }

        public void setItem(ArrayList<Item> item) {
            this.item = item;
        }
    }

    public static class Item {
        private String group;
        private String name;
        private String ndbno;

        public Item(String group, String name, String ndbno) {
            this.group = group;
            this.name = name;
            this.ndbno = ndbno;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNdbno() {
            return ndbno;
        }

        public void setNdbno(String ndbno) {
            this.ndbno = ndbno;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class Foods {
        private FoodItem food;

        public Foods(FoodItem food) {
            this.food = food;
        }

        public FoodItem getFood() {
            return food;
        }

        public void setFood(FoodItem food) {
            this.food = food;
        }
    }

    public static class FoodItem {
        private ArrayList<Nutrient> nutrients;
        private String type;

        public FoodItem(ArrayList<Nutrient> nutrients, String type) {
            this.nutrients = nutrients;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ArrayList<Nutrient> getNutrients() {
            return nutrients;
        }

        public void setNutrients(ArrayList<Nutrient> nutrients) {
            this.nutrients = nutrients;
        }
    }

    public static class Nutrient {
        private int nutrientId;
        private double value;
        private ArrayList<Measure> measures;

        public Nutrient(int nutrientId, double value, ArrayList<Measure> measures) {
            this.nutrientId = nutrientId;
            this.value = value;
            this.measures = measures;
        }

        public int getNutrientId() {
            return nutrientId;
        }

        public void setNutrientId(int nutrientId) {
            this.nutrientId = nutrientId;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public ArrayList<Measure> getMeasures() {
            return measures;
        }

        public void setMeasures(ArrayList<Measure> measures) {
            this.measures = measures;
        }
    }

    public static class Measure {
        private String label;
        private double qty;
        private double value;

        public Measure(String label, double qty, double value) {
            this.label = label;
            this.qty = qty;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getQty() {
            return qty;
        }

        public void setQty(double qty) {
            this.qty = qty;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}


