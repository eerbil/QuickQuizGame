package com.example.eliferbil.quickquiz.QuickQuiz;

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
    private AnswerState answerState = AnswerState.INITIAL;

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

    public AnswerState getAnswerState() {
        return answerState;
    }

    public void setAnswerState(AnswerState answerState) {
        this.answerState = answerState;
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
        dest.writeTypedList(this.answers);
        dest.writeInt(this.answerState == null ? -1 : this.answerState.ordinal());
    }

    protected Question(Parcel in) {
        this.category = in.readString();
        this.text = in.readString();
        this.score = in.readInt();
        this.answers = in.createTypedArrayList(Answer.CREATOR);
        int tmpAnswerState = in.readInt();
        this.answerState = tmpAnswerState == -1 ? null : AnswerState.values()[tmpAnswerState];
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public enum AnswerState {
        INITIAL, CORRECT, INCORRECT, EMPTY
    }
}
