package com.techelevator.tenmo.model;

public class UsernameAndId {
    private String username;
    private int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsernameAndId(String username, int id) {
        this.username = username;
        this.id = id;
    }
}
