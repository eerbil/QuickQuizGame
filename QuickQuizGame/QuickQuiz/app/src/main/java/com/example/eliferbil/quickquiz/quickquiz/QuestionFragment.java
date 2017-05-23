package com.example.eliferbil.quickquiz.quickquiz;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eliferbil.quickquiz.BackPressedListener;
import com.example.eliferbil.quickquiz.R;
import com.example.eliferbil.quickquiz.TransitionManager;

public class QuestionFragment extends Fragment implements BackPressedListener {

    private QuestionPresenter presenter;

    private Button[] btns;
    private TextView timeText;
    public static boolean isRunning;
    private TextView questionText;
    private TextView pointsText;
    private int seconds = 60;
    private boolean ceaseListening = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View root = getView();
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("SECONDS_BUNDLE_KEY");
        }
        // Trivial Field Assignments
        btns = new Button[]{
                (Button) root.findViewById(R.id.answer1),
                (Button) root.findViewById(R.id.answer2),
                (Button) root.findViewById(R.id.answer3),
                (Button) root.findViewById(R.id.answer4),
        };

        timeText = (TextView) root.findViewById(R.id.timer);
        questionText = (TextView) root.findViewById(R.id.question);
        pointsText = (TextView) root.findViewById(R.id.points);

        // Retrieval, Creation & Configuration

        QuestionPresenter.TransitionManager transitionManager;
        Context context = getContext();
        if (context instanceof TransitionManager.Provider) {
            transitionManager = ((TransitionManager.Provider) context).provide(this);
        } else {
            throw new IllegalArgumentException("Must provide TransitionManager");
        }

        presenter = new QuestionPresenter(transitionManager, this);

        View.OnClickListener answerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAnswer((int) v.getTag());
            }
        };

        for (Button btn : btns) {
            btn.setOnClickListener(answerListener);
        }

        // Pass control to Presenter
        presenter.create(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    protected void displayAnswerStatus(int ansOrder, boolean isCorrect) {
        Button ansButton = btns[ansOrder];
        ansButton.setBackgroundColor(isCorrect ? Color.GREEN : Color.RED);
    }

    public void endQuestion() {
        // Prevent users from going back prematurely
        ceaseListening = true;
        // Prevent users from clicking
        for (Button btn : btns) {
            btn.setClickable(false);
        }

        // Finish activity after a specified timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.questionEnded();
            }
        }, 1500);
    }

    protected void showTime(String time) {
        timeText.setText(time);
    }

    public void setAnswerText(int ansOrder, String text) {
        Button btn = btns[ansOrder];
        btn.setText(text);

        // For ease of reverse access
        btn.setTag(ansOrder);
    }

    public void setQuestionText(String questionText) {
        this.questionText.setText(questionText);
    }

    protected void setQuestionPoints(int score) {
        pointsText.setText("This question is worth " + score + " points.");
    }


    @Override
    public boolean onBackPressed() {
        if (!ceaseListening) {
            presenter.onBackPressed();
        }
        return !ceaseListening;
    }
}
