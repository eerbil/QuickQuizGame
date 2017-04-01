package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.eliferbil.quickquiz.QuickQuiz.Game;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }


    @Override
    protected void onStart() {
        super.onStart();

        setInfo();
    }

    private void setInfo() {
        Game game = Game.getInstance();
        User user = game.getUser();
        ((TextView) findViewById(R.id.userInfo)).setText(user.getUsername() + ": " + user.getScore());
    }


    public void onBackClick(View v) {
        Intent backActivity = new Intent(this, UsernameActivity.class);
        startActivity(backActivity);
        finish();
    }
}
