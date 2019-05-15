package com.iest0002.calorietracker.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class User {
    @PrimaryKey
    private int userId;
    private String fname;
    private String lname;
    private String email;
    private Date dob;
    private double height;
    private double weight;
    private char gender;
    private String address;
    private String postcode;
    private int levelOfActivity;
    private int stepsPerMile;

    private double calorieGoal;

    public User(int userId, String fname, String lname, String email, Date dob, double height,
                double weight, char gender, String address, String postcode, int levelOfActivity, int stepsPerMile) {
        this.userId = userId;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepsPerMile = stepsPerMile;
        this.dob = dob;
    }

    public User(String fname, String lname, String email, String dob,
                double height, double weight, char gender, String address,
                String postcode, int levelOfActivity, int stepsPerMile) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepsPerMile = stepsPerMile;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.dob = format.parse(dob);
        } catch (ParseException e) {
            this.dob = new Date();
        }

    }

    public User(int userId, String fname, String lname, String email, Date dob, double height,
                double weight, char gender, String address, String postcode, int levelOfActivity, int stepsPerMile, double calorieGoal) {
        this.userId = userId;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepsPerMile = stepsPerMile;
        this.calorieGoal = calorieGoal;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public int getLevelOfActivity() {
        return levelOfActivity;
    }

    public void setLevelOfActivity(int levelOfActivity) {
        this.levelOfActivity = levelOfActivity;
    }

    public int getStepsPerMile() {
        return stepsPerMile;
    }

    public void setStepsPerMile(int stepsPerMile) {
        this.stepsPerMile = stepsPerMile;
    }
}
