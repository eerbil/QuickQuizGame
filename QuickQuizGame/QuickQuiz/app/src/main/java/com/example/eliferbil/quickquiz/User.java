package com.example.eliferbil.quickquiz;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class User {
    public String username;
    public int score;

    public User(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public User(String username) {
        this(username, 0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        setScore(getScore() + score);
    }
}
