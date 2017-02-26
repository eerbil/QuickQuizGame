package com.example.eliferbil.quickquiz;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UsernameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
    }

    public void onTextClicked(View view){
        final EditText usernameText = (EditText) findViewById(R.id.username);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run () {
                usernameText.setText("");
                handler.post(this);
            }
        });
    }
}
