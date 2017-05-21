package com.example.eliferbil.quickquiz;

import com.example.eliferbil.quickquiz.database.DTOs.DTO;

import java.util.Observable;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class User extends Observable {
    private String username;
    private String email;
    private String name;
    private String surname;
    private String city;
    private int score;
    private int questionsAnswered; // her soru cevaplandığında arttırılacak, 15 olduğunda scoreActivity açılacak

    public User(String username, String email, String name, String surname, String city, int score, int questionsAnswered) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.score = score;
        this.questionsAnswered = questionsAnswered;
    }

    public User(String username, int score) {
        this(username, null, null, null, null, score, 0);
    }

    public User(String username) {
        this(username, 0);
    }

    public User(String username, String email, String name, String surname, String city) {
        this(username, email, name, surname, city, 0, 0);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static DTO.User toDTO(User user) {
        return new DTO.User(user.username, user.email, user.name, user.surname, user.city);
    }

    public static User fromDTO(DTO.User dto) {
        return new User(dto.username, dto.email, dto.name, dto.surname, dto.city);
    }

    public static class Credentials {
        public final String email;
        public final String password;

        public Credentials(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
