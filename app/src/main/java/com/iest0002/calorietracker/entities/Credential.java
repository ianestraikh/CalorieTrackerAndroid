package com.iest0002.calorietracker.entities;

import java.util.Date;

public class Credential {

    private String username;
    private String passwordHash;
    private Date signupDate;
    private User userId;

    public Credential(String username, String passwordHash, Date signupDate, User userId) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.signupDate = signupDate;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
