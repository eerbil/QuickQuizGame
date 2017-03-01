package com.example.eliferbil.quickquiz;

import java.util.Observable;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class User extends Observable {
    public String username;
    public int score;
    private int questionsAnswered; // her soru cevaplandığında arttırılacak, 15 olduğunda scoreActivity açılacak

    public User(String username, int score) {
        this.username = username;
        this.score = score;
        this.questionsAnswered = 0;
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

        setChanged();
        notifyObservers(score);
        clearChanged();
    }

    public void addScore(int score) {
        setScore(getScore() + score);
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }
}
