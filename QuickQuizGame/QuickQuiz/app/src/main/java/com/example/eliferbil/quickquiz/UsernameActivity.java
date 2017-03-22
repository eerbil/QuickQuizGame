package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UsernameActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        listenClickFor(R.id.start);
        Game.getInstance().setUser(new User(""));
    }

    protected void listenClickFor(int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:

                EditText usernameField = (EditText) findViewById(R.id.username);
                Game.getInstance().setUser(new User(usernameField.getText().toString()));
                if(Game.getInstance().getUser().getUsername().equals("")){
                    TextView errorField = (TextView) findViewById(R.id.errorMessage);
                    errorField.setText("Username cannot be empty!");
                    break;
                } else {
                    Intent nextActivity = new Intent(this, MainActivity.class);
                    startActivity(nextActivity);
                    break;
                }
            default:
                break;
        }
    }
}
