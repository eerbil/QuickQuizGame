package com.example.eliferbil.quickquiz.quickquiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.User;
import com.example.eliferbil.quickquiz.database.DbManager;
import com.example.eliferbil.quickquiz.database.ListenerAggregator;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.example.eliferbil.quickquiz.database.DbManager.ResultListener;

public class GameFragment extends Fragment implements Observer {
    private static final Game GAME = Game.getInstance();
    private static final String LAST_BUTTON_BUNDLE_KEY = "last_button";

    @IdRes
    private int lastButtonId;
    private TransitionManager transitionManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TransitionManager.Provider) {
            transitionManager = ((TransitionManager.Provider) context).provide(this);
        } else {
            throw new IllegalArgumentException("Must provide TransitionManager");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity act = getActivity();

        asyncLoadQuestions(act);


        ((Button) act.findViewById(R.id.startOver)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionManager.startOverPressed();
            }
        });

        if (savedInstanceState != null) {
            lastButtonId = savedInstanceState.getInt(LAST_BUTTON_BUNDLE_KEY);
        }

        //bindButtonsToQuestions(getActivity());

        // Observe User for score change
        User user = GAME.getUser();
        user.addObserver(this);
        update(user, user.getScore());
    }

    private void asyncLoadQuestions(final Activity act) {
        final ProgressDialog pd = new ProgressDialog(act);
        pd.setTitle("Loading Questions...");
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.show();

        final int requestedCatCount = 3;
        final ListenerAggregator<List<Question>> aggListener =
                new ListenerAggregator<>(requestedCatCount, new DbManager.RunOnUIListener<>(act, new ResultListener<List<List<Question>>>() {
                    @Override
                    public void onComplete(List<List<Question>> data) {
                        bindButtonsToQuestions(act);
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(
                                act,
                                "Error while getting questions:" + error,
                                Toast.LENGTH_LONG).show();
                    }
                }));
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GAME.getFoodQuestions(new CategoryResultListener(Category.FOOD, aggListener));
                GAME.getHistoryQuestions(new CategoryResultListener(Category.HISTORY, aggListener));
                GAME.getMusicQuestions(new CategoryResultListener(Category.MUSIC, aggListener));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);

    }

    @Override
    public void update(Observable o, Object score) {
        View view = getView();
        if (view != null) {
            ((TextView) view.findViewById(R.id.scoreboard)).setText("Score: " + score);
        }
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
                        transitionManager.questionSelected(questions.get(questionIndex));

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
            transitionManager.gameEnded();
        }
    }


    private enum Category {
        FOOD("food"),
        HISTORY("hist"),
        MUSIC("music");

        private String idRoot;
        private List<Question> questionsFromCategory;


        Category(String idRoot) {
            //this.questionsFromCategory = questionsFromCategory;
            this.idRoot = idRoot;
        }

        public String getIdRoot() {

            return idRoot;
        }

        public List<Question> getQuestionsFromCategory() {
            return questionsFromCategory;
        }

        private void setQuestionsFromCategory(List<Question> questionsFromCategory) {
            this.questionsFromCategory = questionsFromCategory;
        }
    }

    public interface TransitionManager extends com.example.eliferbil.quickquiz.TransitionManager {
        void startOverPressed();

        void questionSelected(Question selected);

        void gameEnded();
    }

    private class CategoryResultListener implements ResultListener<List<Question>> {

        private Category cat;
        private ResultListener<List<Question>> aggregateListener;

        public CategoryResultListener(Category cat, ResultListener<List<Question>> aggregateListener) {
            this.cat = cat;
            this.aggregateListener = aggregateListener;
        }

        @Override
        public void onComplete(List<Question> data) {
            cat.setQuestionsFromCategory(data);
            aggregateListener.onComplete(data);
        }

        @Override
        public void onError(String error) {
            aggregateListener.onError(cat + error + "\n");
        }
    }
}
