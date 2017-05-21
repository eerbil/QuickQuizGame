package com.example.eliferbil.quickquiz.database.DTOs;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;


public final class DTO {

    private DTO() {
        // Static class
    }

    @IgnoreExtraProperties
    public static class Answer {
        public String text;
        public boolean isCorrect;

        public Answer() {
        }

        public Answer(String text, boolean isCorrect) {
            this.text = text;
            this.isCorrect = isCorrect;
        }
    }

    @IgnoreExtraProperties
    public static class Question {
        public String category;
        public String text;
        public int score;
        public List<Answer> answers;

        public Question() {
        }

        public Question(String category, String text, int score, List<Answer> answers) {
            this.category = category;
            this.text = text;
            this.score = score;
            this.answers = answers;
        }
    }

    @IgnoreExtraProperties
    public static class User {
        public String username;
        public String email;
        public String name;
        public String surname;
        public String city;

        public User() {
        }

        public User(String username, String email, String name, String surname, String city) {
            this.username = username;
            this.email = email;
            this.name = name;
            this.surname = surname;
            this.city = city;
        }
    }
}
