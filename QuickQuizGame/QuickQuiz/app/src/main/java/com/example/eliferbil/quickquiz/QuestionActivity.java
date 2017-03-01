package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class QuestionActivity extends AppCompatActivity {
    public static final String QUESTION_KEY = "question";


    private QuestionPresenter presenter;

    private Button[] btns;
    private TextView timeText;
    private TextView questionText;
    private TextView pointsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Trivial Field Assingments
        btns = new Button[]{
                (Button) findViewById(R.id.answer1),
                (Button) findViewById(R.id.answer2),
                (Button) findViewById(R.id.answer3),
                (Button) findViewById(R.id.answer4),
        };

        timeText = (TextView) findViewById(R.id.timer);
        questionText = (TextView) findViewById(R.id.question);
        pointsText = (TextView) findViewById(R.id.points);

        // Retrieval, Creation & Configuration
        Question question = getIntent().getParcelableExtra(QUESTION_KEY);
        presenter = new QuestionPresenter(question, this);

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

    private void displayAnswerStatus(int ansOrder, boolean isCorrect) {
        Button ansButton = btns[ansOrder];
        ansButton.setBackgroundColor(isCorrect ? Color.GREEN : Color.RED);
    }

    public void endQuestion() {
        // Prevent users from clicking
        for (Button btn : btns) {
            btn.setClickable(false);
        }
        // Prepare result (it is an enum specifying the answer was correct or not or unanswered)
        Intent resultIntent = new Intent();
        presenter.onPreResultSend(resultIntent);
        setResult(RESULT_OK, resultIntent);

        // Finish activity after a specified timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);
    }

    private void showTime(String time) {
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

    private void setQuestionPoints(int score) {
        pointsText.setText("This question is worth " + score + " points.");
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
        super.onBackPressed();
    }

    public static class QuestionPresenter {

        private static final String QUESTION_BUNDLE_KEY = "question_bundle";
        private static final String SECONDS_BUNDLE_KEY = "second_bundle";

        public static final String QUESTION_RESULT_KEY = "question_result";
        public static final String ANSWER_STATE_RESULT_KEY = "result";

        private Question question;
        private final QuestionActivity view;
        private Handler handler;

        private int seconds = 60;
        private AnswerState finalState = AnswerState.EMPTY;


        public QuestionPresenter(Question question, QuestionActivity view) {
            this.question = question;
            this.view = view;


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
                    if (seconds > 0) {
                        seconds--;
                    } else { // Time is up

                        endQuestion(AnswerState.EMPTY);

                        seconds = 0;
                    }
                    handler.postDelayed(this, 1000);
                }
            });


        }

        private void endQuestion(AnswerState as) {

            finalState = as;

            // Stop Timer
            handler.removeCallbacksAndMessages(null);

            // Go back to the main page
            view.endQuestion();
        }

        private int answerOrder(Answer q) {
            return question.getAnswers().indexOf(q);
        }

        public void onAnswer(int ansOrder) {
            Answer answer = question.getAnswers().get(ansOrder);
            view.displayAnswerStatus(ansOrder, answer.isCorrect());

            // Show correct answer
            view.displayAnswerStatus(answerOrder(question.correctAnswer()), true);

            endQuestion(answer.isCorrect() ? AnswerState.CORRECT : AnswerState.INCORRECT);

        }

        public void onPreResultSend(Intent resultIntent) {
            resultIntent.putExtra(ANSWER_STATE_RESULT_KEY, finalState.toString());
            resultIntent.putExtra(QUESTION_RESULT_KEY, question);
        }

        public void onSaveInstanceState(Bundle outState) {
            outState.putParcelable(QUESTION_BUNDLE_KEY, question);
            outState.putInt(SECONDS_BUNDLE_KEY, seconds);
        }

        public void onBackPressed() {
            endQuestion(AnswerState.EMPTY);
        }


        public enum AnswerState {
            CORRECT, INCORRECT, EMPTY
        }
    }
}
