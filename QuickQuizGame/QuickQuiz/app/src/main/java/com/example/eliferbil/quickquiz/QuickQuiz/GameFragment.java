package com.example.eliferbil.quickquiz.QuickQuiz;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.User;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GameFragment extends Fragment implements Observer {
    private static final Game GAME = Game.getInstance();
    private static final String LAST_BUTTON_BUNDLE_KEY = "last_button";

    @IdRes
    private int lastButtonId;
    private TransitionListener transitionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        transitionListener = (TransitionListener) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity act = getActivity();
        bindButtonsToQuestions(act);
        ((Button) act.findViewById(R.id.startOver)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionListener.startOverPressed();
            }
        });

        if (savedInstanceState != null) {
            lastButtonId = savedInstanceState.getInt(LAST_BUTTON_BUNDLE_KEY);
        }

        bindButtonsToQuestions(getActivity());

        // Observe User for score change
        User user = GAME.getUser();
        user.addObserver(this);
        update(user, user.getScore());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    private void bindButtonsToQuestions(Activity act) {
        final Resources resources = getResources();
        final String packageName = act.getPackageName();
        for (Category cat : Category.values()) {
            final List<Question> questions = cat.getQuestionsFromCategory();
            for (int i = 0; i < 5; i++) {
                Button b = (Button) act.findViewById(resources.getIdentifier(cat.getIdRoot() + (i + 1), "id", packageName));
                final int questionIndex = i;
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        lastButtonId = v.getId();
                        transitionListener.questionSelected(questions.get(questionIndex));

                    }
                });
            }
        }
    }

    public void onQuestionEnded(Question question) {
        int buttonColor;
        boolean willBeClickable;
        switch (question.getAnswerState()) {
            case CORRECT:
                buttonColor = Color.GREEN;
                willBeClickable = false;
                GAME.getUser().addScore(question.getScore());
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
                throw new IllegalStateException("Unexpected AnswerState");
        }

        GAME.addToQuestionsAnswered(1);
        View lastButton = getView().findViewById(lastButtonId);
        lastButton.setBackgroundColor(buttonColor);
        lastButton.setClickable(willBeClickable);

        if (!GAME.isGameContinued()) {
            transitionListener.gameEnded();
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

    public interface TransitionListener {
        void startOverPressed();

        void questionSelected(Question selected);

        void gameEnded();
    }
}
