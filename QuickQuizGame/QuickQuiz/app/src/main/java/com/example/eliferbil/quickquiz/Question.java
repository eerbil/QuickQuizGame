package com.example.eliferbil.quickquiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class Question implements Parcelable {
    private String category;
    private String text;
    private int score;
    private List<Answer> answers;

    private SoftReference<Answer> correctAnswerCache;

    public Question(String category, String text, int score, List<Answer> answers) {
        this.category = category;
        this.text = text;
        this.score = score;
        this.answers = answers;
    }

    public Answer correctAnswer() {
        Answer correct = correctAnswerCache == null ? null : correctAnswerCache.get();
        if (correct == null) {
            for (Answer ans : answers) {
                if (ans.isCorrect()) {
                    correctAnswerCache = new SoftReference<>(ans);
                    correct = ans;
                    break;
                }
            }
        }
        return correct;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeString(this.text);
        dest.writeInt(this.score);
        dest.writeList(this.answers);
    }

    protected Question(Parcel in) {
        this.category = in.readString();
        this.text = in.readString();
        this.score = in.readInt();
        this.answers = new ArrayList<Answer>();
        in.readList(this.answers, Answer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
