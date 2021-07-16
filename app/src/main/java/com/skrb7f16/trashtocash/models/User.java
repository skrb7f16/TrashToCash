package com.skrb7f16.trashtocash.models;




public class User {
    String userId, username;
    int totalCredits;

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;

    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public User(String userId, String username, int totalCredits) {
        this.userId = userId;
        this.username = username;
        this.totalCredits = totalCredits;
    }
}
