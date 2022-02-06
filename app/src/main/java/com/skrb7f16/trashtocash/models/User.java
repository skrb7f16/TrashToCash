package com.skrb7f16.trashtocash.models;


import java.util.ArrayList;

public class User {
    String userId, username;
    int totalCredits;
    public ArrayList<String>rooms;

    public ArrayList<String> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<String> rooms) {
        this.rooms = rooms;
    }

    public User(String userId, String username, int totalCredits, ArrayList<String> rooms) {
        this.userId = userId;
        this.username = username;
        this.totalCredits = totalCredits;
        this.rooms = rooms;
    }

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
