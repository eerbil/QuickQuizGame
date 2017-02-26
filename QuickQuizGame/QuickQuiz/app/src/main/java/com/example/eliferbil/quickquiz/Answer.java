package com.example.eliferbil.quickquiz;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class Answer {

    public String text;
    public boolean isCorrect;

    public Answer(String text, boolean isCorrect){
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public Answer(String text){
        this.text = text;
        this.isCorrect = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
