package com.example.eliferbil.quickquiz;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

    private int seconds = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        seconds = 60;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        runTimer();
    }

    private void runTimer() {
        final TextView timerText = (TextView)findViewById(R.id.timer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run () {
                int secs = seconds % 60;
                String time = String.format("%d", secs);
                timerText.setText(time);
                if(seconds>0) {
                    seconds--;
                } else {
                    //go back to the main page
                    seconds = 0;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

}
