package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }

    public void onBackClick(View v) {
        Intent backActivity = new Intent(this, UsernameActivity.class);
        startActivity(backActivity);
    }
}
