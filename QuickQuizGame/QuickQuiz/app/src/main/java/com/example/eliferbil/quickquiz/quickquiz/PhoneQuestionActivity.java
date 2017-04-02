package com.example.eliferbil.quickquiz.quickquiz;

import android.content.Intent;
import android.os.Bundle;

import com.example.eliferbil.quickquiz.BackPressedListener;
import com.example.eliferbil.quickquiz.PhoneBaseActivity;
import com.example.eliferbil.quickquiz.R;

public class PhoneQuestionActivity extends PhoneBaseActivity implements QuestionPresenter.TransitionManager {

    public static final String QUESTION_KEY = "question";
    public static final String QUESTION_RESULT_KEY = "question_result";

    private BackPressedListener bpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_question);
    }


    @Override
    public Question getSuppliedQuestion() {
        return getIntent().getParcelableExtra(QUESTION_KEY);
    }

    @Override
    public void setQuestionToSend(Question question) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(QUESTION_RESULT_KEY, question);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void questionEnded() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (bpl == null || bpl.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void setBackPressedListener(BackPressedListener bpl) {
        this.bpl = bpl;
    }
}