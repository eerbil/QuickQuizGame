package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliferbil.quickquiz.database.DbManager;
import com.example.eliferbil.quickquiz.quickquiz.Game;

public class ScoreActivity extends AppCompatActivity {

    private final Game game = Game.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        saveCurrent();
    }

    private void saveCurrent() {
        User user = game.getUser();
        user.addToOnlineScore(user.getScore());

        DbManager.Provider.getDefault().saveCurrentUser(user, new DbManager.ResultListener<User>() {
            private void display(String message) {
                Toast.makeText(ScoreActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(User data) {
                display("Saved Scores to Server");
            }

            @Override
            public void onError(String error) {
                display("Error saving scores");
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        setInfo();
    }

    private void setInfo() {
        User user = game.getUser();
        ((TextView) findViewById(R.id.userInfo)).setText(user.getUsername() + ": " + user.getScore());
    }


    public void onBackClick(View v) {
        game.setUser(game.getUser().newWithResetTransientStats());

        Intent backActivity = new Intent(this, MainActivity.class);
        startActivity(backActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        onBackClick(null);
    }
}
