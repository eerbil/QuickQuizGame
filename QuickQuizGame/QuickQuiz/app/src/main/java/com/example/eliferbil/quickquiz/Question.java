package com.example.eliferbil.quickquiz;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class Question {
    public String category;
    public String text;
    public int score;
    public List<Answer> answers;

    public Question(String category, String text, int score, List<Answer> answers) {
        this.category = category;
        this.text = text;
        this.score = score;
        this.answers = answers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Answer first, Answer second, Answer third, Answer fourth) {
        List<Answer> answers = new ArrayList<Answer>();
        answers.add(first);
        answers.add(second);
        answers.add(third);
        answers.add(fourth);
        this.answers = answers;

    }

}
