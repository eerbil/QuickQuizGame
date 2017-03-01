package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    private static final int QUESTION_REQUEST = 0;
    private static final Game GAME = Game.getInstance();
    private static final String LAST_BUTTON_BUNDLE_KEY = "last_button";

    @IdRes
    private int lastButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bindButtonsToQuestions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_BUTTON_BUNDLE_KEY, lastButtonId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastButtonId = savedInstanceState.getInt(LAST_BUTTON_BUNDLE_KEY);
    }

    private void bindButtonsToQuestions() {
        final Resources resources = getResources();
        final String packageName = getPackageName();
        for (Category cat : Category.values()) {
            final List<Question> questions = cat.getQuestionsFromCategory();
            for (int i = 0; i < 5; i++) {
                Button b = (Button) findViewById(resources.getIdentifier(cat.getIdRoot() + (i + 1), "id", packageName));
                final int questionIndex = i;
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        lastButtonId = v.getId();
                        Intent intent = new Intent(GameActivity.this, QuestionActivity.class);
                        intent.putExtra(QuestionActivity.QUESTION_KEY, questions.get(questionIndex));
                        startActivityForResult(intent, QUESTION_REQUEST);
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QUESTION_REQUEST && resultCode == RESULT_OK && data != null) {
            Question q = data.getParcelableExtra(QuestionActivity.QuestionPresenter.QUESTION_RESULT_KEY);
            QuestionActivity.QuestionPresenter.AnswerState answerState = QuestionActivity.QuestionPresenter.AnswerState.valueOf(
                    data.getStringExtra(QuestionActivity.QuestionPresenter.ANSWER_STATE_RESULT_KEY));

            int buttonColor;
            switch (answerState) {
                case CORRECT:
                    buttonColor = Color.GREEN;
                    GAME.getUser().addScore(q.getScore());
                    break;
                case INCORRECT:
                    buttonColor = Color.RED;
                    break;
                case EMPTY:
                    buttonColor = Color.BLUE;
                    break;
                default:
                    throw new IllegalStateException("No such AnswerState");
            }
            findViewById(lastButtonId).setBackgroundColor(buttonColor);
        }
    }

    private enum Category {
        FOOD("food", GAME.getFoodQuestions()),
        HISTORY("hist", GAME.getHistoryQuestions()),
        MUSIC("music", GAME.getMusicQuestions());

        private String idRoot;
        private List<Question> questionsFromCategory;


        Category(String idRoot, List<Question> questionsFromCategory) {
            this.questionsFromCategory = questionsFromCategory;
            this.idRoot = idRoot;
        }

        public String getIdRoot() {

            return idRoot;
        }

        public List<Question> getQuestionsFromCategory() {
            return questionsFromCategory;
        }
    }
}
