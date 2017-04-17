package com.example.eliferbil.quickquiz.quickquiz;

import android.os.Bundle;
import android.os.Handler;

import com.example.eliferbil.quickquiz.BackPressedListener;

import java.util.List;

/**
 * Created by Ata on 2.4.2017.
 */
public class QuestionPresenter {

    private static final String QUESTION_BUNDLE_KEY = "question_bundle";
    private static final String SECONDS_BUNDLE_KEY = "second_bundle";

    private TransitionManager transitionManager;

    private Question question;
    private final QuestionFragment view;
    private Handler handler;

    private int seconds = 60;
    private Question.AnswerState finalState = Question.AnswerState.EMPTY;


    public QuestionPresenter(TransitionManager transitionManager, QuestionFragment view) {
        this.transitionManager = transitionManager;
        this.question = transitionManager.getSuppliedQuestion();
        this.view = view;

        transitionManager.setBackPressedListener(view);
    }

    public void create(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.seconds = savedInstanceState.getInt(SECONDS_BUNDLE_KEY);
            this.question = savedInstanceState.getParcelable(QUESTION_BUNDLE_KEY);
        }

        view.setQuestionText(question.getText());
        view.setQuestionPoints(question.getScore());

        List<Answer> answers = question.getAnswers();
        final int length = answers.size();
        for (int i = 0; i < length; i++) {
            Answer ans = answers.get(i);
            view.setAnswerText(i, ans.getText());
        }

        runTimer();
    }

    private void runTimer() {

        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int secs = seconds;
                String time = String.format("%d", secs);
                view.showTime(time);
                if (QuestionFragment.isRunning) {
                    if (seconds > 0) {
                        seconds--;
                    } else { // Time is up
                        endQuestion(Question.AnswerState.EMPTY);
                        // seconds = 0;
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });

    }

    private void endQuestion(Question.AnswerState as) {

        // Show correct answer
        view.displayAnswerStatus(answerOrder(question.correctAnswer()), true);

        finalState = as;
        question.setAnswerState(as);

        // Stop Timer
        handler.removeCallbacksAndMessages(null);


        transitionManager.setQuestionToSend(question);
        // Go back to the main page
        view.endQuestion();
    }

    private int answerOrder(Answer q) {
        return question.getAnswers().indexOf(q);
    }

    public void onAnswer(int ansOrder) {
        Answer answer = question.getAnswers().get(ansOrder);
        view.displayAnswerStatus(ansOrder, answer.isCorrect());

        endQuestion(answer.isCorrect() ? Question.AnswerState.CORRECT : Question.AnswerState.INCORRECT);

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(QUESTION_BUNDLE_KEY, question);
        outState.putInt(SECONDS_BUNDLE_KEY, seconds);
    }

    public void onBackPressed() {
        endQuestion(Question.AnswerState.EMPTY);
    }

    public void questionEnded() {
        transitionManager.setBackPressedListener(null);
        transitionManager.questionEnded();
    }

    public interface TransitionManager extends com.example.eliferbil.quickquiz.TransitionManager {
        Question getSuppliedQuestion();

        void setQuestionToSend(Question question);

        void questionEnded();

        void setBackPressedListener(BackPressedListener bpl);
    }
}
