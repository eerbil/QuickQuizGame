package com.example.eliferbil.quickquiz.quickquiz;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eliferbil.quickquiz.database.DTOs.DTO;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class Answer implements Parcelable {

    public static Answer fromDTO(DTO.Answer dto) {
        return dto != null ? new Answer(dto.text, dto.isCorrect) : null;
    }

    public static DTO.Answer toDTO(Answer answer) {
        return answer != null ? new DTO.Answer(answer.text, answer.isCorrect) : null;
    }

    private String text;
    private boolean isCorrect;

    public Answer(String text, boolean isCorrect){
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public Answer(String text){
        this.text = text;
        this.isCorrect = false;
    }

    protected Answer(Parcel in) {
        this.text = in.readString();
        this.isCorrect = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeByte(this.isCorrect ? (byte) 1 : (byte) 0);
    }


    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}
