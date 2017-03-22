package com.example.eliferbil.quickquiz;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static android.app.Activity.RESULT_OK;

public class GameFragment extends Fragment implements Observer {
    private static final int QUESTION_REQUEST = 0;
    private static final Game GAME = Game.getInstance();
    private static final String LAST_BUTTON_BUNDLE_KEY = "last_button";

    @IdRes
    private int lastButtonId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindButtonsToQuestions();

        // Observe User for score change
        User user = GAME.getUser();
        user.addObserver(this);
        update(user, user.getScore());
        return inflater.inflate(R.layout.activity_game, container, false);
    }

    @Override
    public void update(Observable o, Object score) {
        ((TextView) getView().findViewById(R.id.scoreboard)).setText("Score: " + score);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_BUTTON_BUNDLE_KEY, lastButtonId);
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        lastButtonId = savedInstanceState.getInt(LAST_BUTTON_BUNDLE_KEY);
    }

    private void bindButtonsToQuestions() {
  /*      final Resources resources = getResources();
        final String packageName = getPackageName();
        for (Category cat : Category.values()) {
            final List<Question> questions = cat.getQuestionsFromCategory();
            for (int i = 0; i < 5; i++) {
                Button b = (Button) getView().findViewById(resources.getIdentifier(cat.getIdRoot() + (i + 1), "id", packageName));
                final int questionIndex = i;
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        lastButtonId = v.getId();
                        // Intent intent = new Intent(this, QuestionActivity.class);
                        // intent.putExtra(QuestionActivity.QUESTION_KEY, questions.get(questionIndex));
                        // startActivityForResult(intent, QUESTION_REQUEST);
                    }
                });
            }
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QUESTION_REQUEST && resultCode == RESULT_OK && data != null) {
            Question q = data.getParcelableExtra(QuestionActivity.QuestionPresenter.QUESTION_RESULT_KEY);
            QuestionActivity.QuestionPresenter.AnswerState answerState = QuestionActivity.QuestionPresenter.AnswerState.valueOf(
                    data.getStringExtra(QuestionActivity.QuestionPresenter.ANSWER_STATE_RESULT_KEY));

            int buttonColor;
            boolean willBeClickable;
            switch (answerState) {
                case CORRECT:
                    buttonColor = Color.GREEN;
                    willBeClickable = false;
                    GAME.getUser().addScore(q.getScore());
                    break;
                case INCORRECT:
                    willBeClickable = false;
                    buttonColor = Color.RED;
                    break;
                case EMPTY:
                    willBeClickable = false;
                    buttonColor = Color.BLUE;
                    break;
                default:
                    throw new IllegalStateException("No such AnswerState");
            }

            GAME.addToQuestionsAnswered(1);
            View lastButton = getView().findViewById(lastButtonId);
            lastButton.setBackgroundColor(buttonColor);
            lastButton.setClickable(willBeClickable);

            if (!GAME.isGameContinued()) {
                //Intent intent = new Intent(this, ScoreActivity.class);
                //startActivity(intent);
                //finish();
            }
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

    public void onBackClick(View v) {
        //Intent backActivity = new Intent(this, UsernameActivity.class);
        //startActivity(backActivity);
    }
}
